package com.CustomerRelationshipManagement.controller;



import com.CustomerRelationshipManagement.entity.UserEntity; // Import your User entity

import com.CustomerRelationshipManagement.entity.TaskEntity; // Import your Task entity

import com.CustomerRelationshipManagement.service.UserService; // Import your UserService

import com.CustomerRelationshipManagement.service.TaskService; // Import your TaskService

import org.json.JSONArray;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.client.RestTemplate;



import java.util.Collections;

import java.util.List;

import java.util.Map;

import java.util.stream.Collectors;



@RestController

public class AiReportController {



// Injects the API key from your application.properties

    @Value("${GEMINI_API_KEY}")

    private String geminiApiKey;



// Injects your existing services so the agent can get live data

    @Autowired

    private UserService userService;



    @Autowired

    private TaskService taskService;



    @PostMapping("/api/generate-report")

    public ResponseEntity<Map<String, String>> generateReport(@RequestBody ReportRequest request) {

        try {

// STEP 1: Gather live data from your services based on the prompt.

// This is a simple example; a more advanced agent could parse the prompt

// to decide which data to fetch.

            List<UserEntity> users = userService.getAllUsers(); // Assuming you have such a method

            List<TaskEntity> tasks = taskService.getAllTasks(); // Assuming you have such a method



// STEP 2: Format the live data into a text block for the AI.

            String dataContext = buildDataContext(users, tasks);



// STEP 3: Create a new, more powerful prompt that includes the live data.

            String finalPrompt = "Based on the following data from our CRM, please answer this question: '" + request.getPrompt() + "'\n\n"

                    + "--- DATA START ---\n"

                    + dataContext

                    + "\n--- DATA END ---";



// STEP 4: Call the Gemini API with the data-rich prompt.

            RestTemplate restTemplate = new RestTemplate();

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey;



            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);



            String requestBody = new JSONObject()

                    .put("contents", new JSONArray()

                            .put(new JSONObject()

                                    .put("parts", new JSONArray()

                                            .put(new JSONObject().put("text", finalPrompt))

                                    )

                            )

                    ).toString();



            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);



            JSONObject responseJson = new JSONObject(response.getBody());

            String reportText = responseJson.getJSONArray("candidates")

                    .getJSONObject(0)

                    .getJSONObject("content")

                    .getJSONArray("parts")

                    .getJSONObject(0)

                    .getString("text");



            return ResponseEntity.ok(Collections.singletonMap("report", reportText));



        } catch (Exception e) {

            System.err.println("Error in AI Controller: " + e.getMessage());

            e.printStackTrace(); // This will print the full error in your Spring Boot console

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)

                    .body(Collections.singletonMap("report", "Error: Could not generate report. Check API key and backend logs."));

        }

    }



// Helper method to convert your live data into a simple text format for the AI

    private String buildDataContext(List<UserEntity> users, List<TaskEntity> tasks) {

        StringBuilder sb = new StringBuilder();



        sb.append("Total Users: ").append(users.size()).append("\n");

        long activeUsers = users.stream().filter(u -> "active".equalsIgnoreCase(u.getStatus())).count();

        sb.append("Active Users: ").append(activeUsers).append("\n\n");



        sb.append("Total Tasks: ").append(tasks.size()).append("\n");

        Map<String, Long> tasksByStatus = tasks.stream()

                .collect(Collectors.groupingBy(TaskEntity::getStatus, Collectors.counting()));

        sb.append("Tasks by Status: ").append(tasksByStatus.toString()).append("\n");



        return sb.toString();

    }



// A simple class to map the incoming JSON from the frontend

    static class ReportRequest {

        private String prompt;

        public String getPrompt() { return prompt; }

        public void setPrompt(String prompt) { this.prompt = prompt; }

    }

}