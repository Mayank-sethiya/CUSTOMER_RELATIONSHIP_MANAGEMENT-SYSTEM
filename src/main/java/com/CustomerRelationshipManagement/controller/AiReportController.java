package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.*;
import com.CustomerRelationshipManagement.service.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reports")
public class AiReportController {

    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;

    @Autowired private UserService userService;
    @Autowired private TaskService taskService;
    @Autowired private ContactService contactService;
    @Autowired private BroadcastService broadcastService;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateReport(@RequestBody ReportRequest request) {
        try {
            String prompt = (request.getPrompt() == null ? "" : request.getPrompt().trim()).toLowerCase();

            // fetch CRM live data
            List<UserEntity> users = userService.getAllUsers();
            List<TaskEntity> tasks = taskService.getAllTasks();
            List<ContactEntity> contacts = contactService.getAllContacts();
            List<BroadcastEntity> broadcasts = broadcastService.getAllMessages();

            // handle direct structured queries
            String direct = handleDirectQueries(prompt, users, tasks, contacts, broadcasts);
            if (direct != null) return ok(direct);

            // fallback → build context for AI
            String dataContext = buildDataContext(users, tasks, contacts, broadcasts);
            String ai = callGemini(request.getPrompt(), dataContext);
            return ok(ai);

        } catch (Exception e) {
            e.printStackTrace();
            return error("⚠️ AI error: Could not generate report.");
        }
    }

    // -------- DIRECT QUERY HANDLERS --------
    private String handleDirectQueries(String prompt,
                                       List<UserEntity> users,
                                       List<TaskEntity> tasks,
                                       List<ContactEntity> contacts,
                                       List<BroadcastEntity> broadcasts) {

        // synonyms mapping
        if (contains(prompt, "total users", "how many users", "number of users")) {
            return "👥 Total Users: " + users.size();
        }
        if (contains(prompt, "active users", "currently active", "logged in users")) {
            long count = users.stream().filter(u -> "active".equalsIgnoreCase(u.getStatus())).count();
            return "🟢 Active Users: " + count;
        }
        if (contains(prompt, "total leads", "total contacts")) {
            return "📇 Total Leads/Contacts: " + contacts.size();
        }
        if (contains(prompt, "signups today", "new users today", "registered today")) {
            LocalDate today = LocalDate.now();
            long count = users.stream().filter(u -> today.equals(u.getCreatedAt())).count();
            return "🆕 Signups Today: " + count;
        }
        if (contains(prompt, "pending tasks", "incomplete tasks")) {
            long count = tasks.stream().filter(t -> "pending".equalsIgnoreCase(t.getStatus())).count();
            return "⏳ Pending Tasks: " + count;
        }
        if (contains(prompt, "completed tasks", "done tasks", "finished tasks")) {
            long count = tasks.stream().filter(t -> "completed".equalsIgnoreCase(t.getStatus())).count();
            return "✅ Completed Tasks: " + count;
        }
        if (contains(prompt, "total broadcasts", "sent messages", "announcements")) {
            return "📢 Total Broadcasts: " + broadcasts.size();
        }

        // per-user lookups by email/username
        Optional<UserEntity> uMatch = findUserByPrompt(users, prompt);
        if (uMatch.isPresent()) {
            UserEntity u = uMatch.get();
            List<TaskEntity> assigned = tasks.stream()
                    .filter(t -> t.getAssignedTo() != null && t.getAssignedTo().contains(u.getEmail()))
                    .toList();
            long completed = assigned.stream().filter(t -> "completed".equalsIgnoreCase(t.getStatus())).count();
            long pending = assigned.stream().filter(t -> "pending".equalsIgnoreCase(t.getStatus())).count();

            return "👤 User Report\n" +
                    "• Username: " + safe(u.getUsername()) + "\n" +
                    "• Email: " + safe(u.getEmail()) + "\n" +
                    "• Status: " + safe(u.getStatus()) + "\n" +
                    "• Joined: " + (u.getCreatedAt() != null ? u.getCreatedAt().toString() : "unknown") + "\n" +
                    "• Tasks Assigned: " + assigned.size() +
                    " (✅ " + completed + " completed, ⏳ " + pending + " pending)";
        }

        if (contains(prompt, "summary", "report", "overview")) {
            return null; // let AI generate full summary
        }

        return null;
    }

    // -------- CONTEXT FOR AI --------
    private String buildDataContext(List<UserEntity> users, List<TaskEntity> tasks,
                                    List<ContactEntity> contacts, List<BroadcastEntity> broadcasts) {

        StringBuilder sb = new StringBuilder();

        sb.append("=== USERS ===\n");
        for (UserEntity u : users) {
            sb.append("Username: ").append(safe(u.getUsername()))
                    .append(" | Email: ").append(safe(u.getEmail()))
                    .append(" | Status: ").append(safe(u.getStatus()))
                    .append(" | Joined: ").append(u.getCreatedAt() != null ? u.getCreatedAt() : "unknown")
                    .append("\n");
        }

        sb.append("\n=== TASKS ===\n");
        for (TaskEntity t : tasks) {
            sb.append("Task: ").append(safe(t.getTitle()))
                    .append(" | Status: ").append(safe(t.getStatus()))
                    .append(" | AssignedTo: ").append(safe(t.getAssignedTo()))
                    .append(" | AssignedBy: ").append(safe(t.getAssignedBy()))
                    .append("\n");
        }

        sb.append("\n=== CONTACTS ===\n");
        for (ContactEntity c : contacts) {
            sb.append("Name: ").append(safe(c.getName()))
                    .append(" | Email: ").append(safe(c.getEmail()))
                    .append(" | Company: ").append(safe(c.getCompany()))
                    .append(" | Status: ").append(safe(c.getStatus()))
                    .append("\n");
        }

        sb.append("\n=== BROADCASTS ===\n");
        for (BroadcastEntity b : broadcasts) {
            sb.append("Subject: ").append(safe(b.getSubject()))
                    .append(" | Type: ").append(safe(b.getType()))
                    .append(" | Status: ").append(safe(b.getStatus()))
                    .append(" | Recipients: ").append(String.join(", ", b.getRecipients()))
                    .append("\n");
        }

        return sb.toString();
    }

    // -------- GEMINI AI CALL --------
    private String callGemini(String userPrompt, String dataContext) {
        String finalPrompt =
                "You are an intelligent CRM assistant. Always ground your answers in the given CRM data.\n" +
                        "For structured queries (counts, lists) → give precise answers.\n" +
                        "For per-user reports → include username, email, status, join date, tasks.\n" +
                        "For summary reports → generate a business-like structured overview of users, tasks, contacts, broadcasts.\n\n" +
                        "----- DATA START -----\n" + dataContext + "\n----- DATA END -----\n\n" +
                        "Question: " + userPrompt;

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject().put("text", finalPrompt)))))
                .toString();

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            return "⚠️ AI error: " + response.getStatusCode();
        }

        JSONObject responseJson = new JSONObject(response.getBody());
        try {
            return responseJson.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");
        } catch (Exception ex) {
            return "⚠️ AI parse error: " + response.getBody();
        }
    }

    // -------- HELPERS --------
    private boolean contains(String text, String... phrases) {
        for (String p : phrases) if (text.contains(p)) return true;
        return false;
    }
    private Optional<UserEntity> findUserByPrompt(List<UserEntity> users, String prompt) {
        return users.stream().filter(u ->
                prompt.contains(safe(u.getUsername()).toLowerCase()) ||
                        prompt.contains(safe(u.getEmail()).toLowerCase())
        ).findFirst();
    }
    private String safe(String s) { return s == null ? "" : s.trim(); }
    private ResponseEntity<Map<String, String>> ok(String msg) { return ResponseEntity.ok(Collections.singletonMap("report", msg)); }
    private ResponseEntity<Map<String, String>> error(String msg) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("report", msg)); }

    public static class ReportRequest {
        private String prompt;
        public String getPrompt() { return prompt; }
        public void setPrompt(String prompt) { this.prompt = prompt; }
    }
}
