package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.UserEntity;
import com.CustomerRelationshipManagement.repository.UserRepository;
import com.CustomerRelationshipManagement.service.ContactService;
import com.CustomerRelationshipManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactService contactService;



    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        try {
            UserEntity newUser = userService.registerUser(user);

            // Check and qualify lead if email matches
            contactService.qualifyLeadByEmail(user.getEmail());

            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @PostMapping("/login")
    public UserEntity login(@RequestBody UserEntity user) {
        UserEntity existing = userService.loginUser(user.getUsername(), user.getPassword());
        if (existing != null) {
            existing.setStatus("active");
            userRepository.save(existing);
            return existing; // 🔁 Return full user info
        } else {
            return null;
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestBody UserEntity user) {
        UserEntity existing = userRepository.findByUsername(user.getUsername());
        if (existing != null) {
            existing.setStatus("inactive");
            userRepository.save(existing);
            return "Logout successful!";
        } else {
            return "Logout failed.";
        }
    }



    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }
    // DELETE user by ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // UPDATE user
    @PutMapping("/{id}")
    public UserEntity updateUser(@PathVariable Long id, @RequestBody UserEntity user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/search")
    public List<UserEntity> search(@RequestParam String keyword) {
        return userService.searchUsers(keyword);
    }
    @GetMapping("/count")
    public long getUserCount() {
        return userService.countUsers();
    }



    @GetMapping("/count/recent")
    public long getRecentSignupCount() {
        LocalDateTime last24hrs = LocalDateTime.now().minusHours(24);
        return userRepository.countByCreatedAtAfter(last24hrs);
    }

    @GetMapping("/signups/monthly")
    public Map<String, Object> getMonthlySignups() {
        return userService.getMonthlySignupData();
    }





}


