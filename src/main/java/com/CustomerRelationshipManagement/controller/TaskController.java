
package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.TaskEntity;
import com.CustomerRelationshipManagement.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import com.CustomerRelationshipManagement.repository.FileStorageService;


@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService service;

    @PostMapping("/assign")
    public List<TaskEntity> assignTask(
            @RequestParam("taskData") String taskDataJson,
            @RequestParam(value = "file_0", required = false) MultipartFile[] files) throws IOException {

        // Parse JSON string to a Map
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> payload = mapper.readValue(taskDataJson, Map.class);

        // Extract form fields
        List<String> users = (List<String>) payload.get("assignedTo");
        String title = (String) payload.get("title");
        String description = (String) payload.get("description");
        String priority = (String) payload.get("priority");
        String dueDateStr = (String) payload.get("dueDate");
        LocalDate dueDate = (dueDateStr != null && !dueDateStr.isBlank()) ? LocalDate.parse(dueDateStr) : null;

        // Upload files to S3 and collect public URLs
        List<String> fileUrls = new ArrayList<>();
        if (files != null && files.length > 0) {
            fileUrls = fileStorageService.saveFiles(files); // ✅ uploads to S3, returns URLs
        }

        // Pass to your service layer to save the task
        return service.assignTask(title, description, priority, dueDate, users, fileUrls);
    }


    @GetMapping("/user/{email}")
    public List<TaskEntity> getUserTasks(@PathVariable String email) {
        System.out.println("Getting tasks for user: " + email);
        List<TaskEntity> tasks = service.getTasksByUser(email);
        System.out.println("Returning " + tasks.size() + " tasks");
        return tasks;
    }

    // Add this method for debugging
    @GetMapping("/all")
    public List<TaskEntity> getAllTasks() {
        return service.getAllTasks();
    }

    @PutMapping("/{id}/complete")
    public TaskEntity markTaskComplete(@PathVariable Long id) {
        return service.markComplete(id);
    }

    @PutMapping("/{id}/incomplete")
    public TaskEntity markTaskIncomplete(@PathVariable Long id) {
        return service.markIncomplete(id);
    }

    @PutMapping("/{id}/inprogress")
    public TaskEntity markTaskInProgress(@PathVariable Long id) {
        return service.markInProgress(id);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> softDeleteTask(@PathVariable Long id) {
        service.softDeleteTask(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<?> softDeleteAllTasks() {
        service.softDeleteAllTasks();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics")
    public Map<String, Object> getTaskStatistics() {
        return service.getTaskStatistics();
    }

    @GetMapping("/recent")
    public List<Map<String, ? extends Serializable>> getRecentAssignedTasks() {
        List<TaskEntity> tasks = service.getRecentAssignedTasks();

        return tasks.stream()
                .filter(task -> !"deleted".equals(task.getStatus())) // Filter out deleted tasks
                .map(task -> Map.of(
                        "id", (Serializable) task.getId(),
                        "title", (Serializable) task.getTitle(),
                        "assignedTo", (Serializable) task.getAssignedTo(),
                        "totalAssignees", (Serializable) (task.getAssignedTo() != null ? task.getAssignedTo().split(",").length - 2 : 0), // -2 for leading and trailing commas
                        "createdAt", (Serializable) task.getCreatedAt(),
                        "status", (Serializable) task.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Autowired
    private FileStorageService fileStorageService; // ✅ Correct


    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files") MultipartFile[] files) throws IOException {
        List<String> urls = fileStorageService.saveFiles(files);
        return ResponseEntity.ok(urls);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable Long id) {
        TaskEntity task = service.getTaskById(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", task.getId());
        response.put("title", task.getTitle());
        response.put("description", task.getDescription());
        response.put("priority", task.getPriority());
        response.put("dueDate", task.getDueDate());
        response.put("status", task.getStatus());
        response.put("assignedTo", task.getAssignedTo());
        response.put("createdAt", task.getCreatedAt());

        // Attachments
        if (task.getFileUrls() != null && !task.getFileUrls().isBlank()) {
            List<Map<String, String>> attachments = Arrays.stream(task.getFileUrls().split(","))
                    .filter(url -> !url.isBlank())
                    .map(url -> {
                        String filename = Paths.get(url).getFileName().toString();
                        // Serve using your file endpoint
                        String fileUrl = "http://localhost:8080/api/tasks/files/" + filename;
                        Map<String, String> entry = new HashMap<>();
                        entry.put("name", filename);
                        entry.put("url", fileUrl);
                        return entry;
                    })
                    .collect(Collectors.toList());
            response.put("attachments", attachments);
        } else {
            response.put("attachments", List.of());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }








}



