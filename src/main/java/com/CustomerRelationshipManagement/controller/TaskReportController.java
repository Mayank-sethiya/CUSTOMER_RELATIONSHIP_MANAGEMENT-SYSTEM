package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.TaskReportEntity;
import com.CustomerRelationshipManagement.repository.TaskReportRepository;
import com.CustomerRelationshipManagement.repository.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class TaskReportController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private TaskReportRepository taskReportRepository;

    // 🌟 Submit a report with optional file uploading to S3
    @PostMapping
    public ResponseEntity<String> submitReport(
            @RequestParam("taskId") Long taskId,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userName") String userName,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws IOException {

        String fileUrl = null;

        // ✔️ Upload file to AWS S3 if provided
        if (file != null && !file.isEmpty()) {
            fileUrl = fileStorageService.saveFiles(new MultipartFile[]{file}).get(0); // Returns the first file's S3 URL
        }

        // Create report object and save
        TaskReportEntity report = new TaskReportEntity();
        report.setTaskId(taskId);
        report.setTitle(title);
        report.setDescription(description);
        report.setUserEmail(userEmail);
        report.setUserName(userName);
        report.setFileUrl(fileUrl);

        taskReportRepository.save(report);
        return ResponseEntity.ok("Report submitted successfully!");
    }

    // 🌟 Get reports (with optional filters)
    @GetMapping
    public ResponseEntity<List<TaskReportEntity>> getReports(
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) String userId) {

        if ((userEmail == null || userEmail.isBlank()) &&
                (userId == null || userId.isBlank())) {
            return ResponseEntity.ok(taskReportRepository.findAll());
        }

        List<TaskReportEntity> reports = taskReportRepository.searchReports(userEmail, userId);
        return ResponseEntity.ok(reports);
    }
}
