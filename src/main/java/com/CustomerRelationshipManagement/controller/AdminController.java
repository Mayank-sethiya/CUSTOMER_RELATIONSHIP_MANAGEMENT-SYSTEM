package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.AdminEntity;
import com.CustomerRelationshipManagement.entity.UserEntity;
import com.CustomerRelationshipManagement.repository.UserRepository;
import com.CustomerRelationshipManagement.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserRepository userRepo;


    @PostMapping("/register")
    public AdminEntity register(@RequestBody AdminEntity user) {
        return adminService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminEntity user) {
        AdminEntity admin = adminService.login(user.getUsername(), user.getPassword());
        if (admin != null) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin credentials are wrong");
        }
    }








}
