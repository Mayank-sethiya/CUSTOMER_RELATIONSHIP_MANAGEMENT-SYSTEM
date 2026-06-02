
package com.CustomerRelationshipManagement.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import com.CustomerRelationshipManagement.entity.TaskEntity;
import com.CustomerRelationshipManagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskRepository repository;


    public List<TaskEntity> getTasksByUser(String email) {
        String emailWithComma = "," + email + ",";
        return taskRepository.findByAssignedToAndAssignedByAndStatusNotDeleted(emailWithComma, "admin@gmail.com");
    }


    public List<TaskEntity> assignTask(String title, String description, String priority,
                                       LocalDate dueDate, List<String> users, List<String> fileUrls) {
        List<TaskEntity> savedTasks = new ArrayList<>();
        String assignedEmails = "," + String.join(",", users) + ",";

        TaskEntity task = new TaskEntity();
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setAssignedTo(assignedEmails);
        task.setStatus("pending"); // Set default status
        task.setCreatedAt(LocalDate.now()); // Set creation date
        task.setAssignedBy("admin@gmail.com"); // or use currently logged-in admin email
        task.setFileUrls(String.join(",", fileUrls));

        System.out.println("Creating task: " + title + " for users: " + assignedEmails);
        TaskEntity savedTask = taskRepository.save(task);
        savedTasks.add(savedTask);
        System.out.println("Task saved with ID: " + savedTask.getId());

        return savedTasks;
    }

    public TaskEntity markComplete(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();
        task.setStatus("completed");
        return taskRepository.save(task);
    }

    public TaskEntity markIncomplete(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();
        task.setStatus("pending");
        return taskRepository.save(task);
    }

    // Add method to get all tasks for debugging
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();

    }
    public TaskEntity markInProgress(Long id) {
        TaskEntity task = repository.findById(id).orElseThrow();
        task.setStatus("in-progress");
        task.setInProgressStartTime(LocalDateTime.now());
        return repository.save(task);

    }

    public void softDeleteTask(Long id) {
        TaskEntity task = taskRepository.findById(id).orElseThrow();
        task.setStatus("deleted");
        taskRepository.save(task);
    }

    public void softDeleteAllTasks() {
        List<TaskEntity> allTasks = taskRepository.findAll();
        allTasks.forEach(task -> task.setStatus("deleted"));
        taskRepository.saveAll(allTasks);
    }

    public Map<String, Object> getTaskStatistics() {
        List<TaskEntity> allTasks = taskRepository.findAll();

        long todayCount = allTasks.stream()
                .filter(task -> !"deleted".equals(task.getStatus()))
                .filter(task -> task.getCreatedAt().equals(LocalDate.now()))
                .count();

        long pendingCount = allTasks.stream()
                .filter(task -> "pending".equals(task.getStatus()))
                .count();

        long completedCount = allTasks.stream()
                .filter(task -> "completed".equals(task.getStatus()))
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("todayTaskCount", todayCount);
        stats.put("pendingTaskCount", pendingCount);
        stats.put("completedTaskCount", completedCount);

        return stats;
    }

    public List<TaskEntity> getRecentAssignedTasks() {
        return taskRepository.findTop10ByStatusNotOrderByCreatedAtDesc("deleted");
    }

    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }






}
