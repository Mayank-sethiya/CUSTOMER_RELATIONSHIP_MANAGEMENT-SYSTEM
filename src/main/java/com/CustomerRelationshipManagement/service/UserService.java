package com.CustomerRelationshipManagement.service;

import com.CustomerRelationshipManagement.entity.UserEntity;
import com.CustomerRelationshipManagement.repository.MonthlySignup;
import com.CustomerRelationshipManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    // 1. INJECT THE CONTACT SERVICE SO YOU CAN USE IT
    @Autowired
    private ContactService contactService;

    public UserEntity registerUser(UserEntity user) {
        String email = user.getEmail();
        String username = user.getUsername();

        // (All your existing validation checks remain here...)
        if (!email.endsWith("@gmail.com")) {
            throw new RuntimeException("Only @gmail.com emails are allowed");
        }
        if (email.contains(" ")) {
            throw new RuntimeException("Email cannot contain spaces");
        }
        if (userRepo.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (username.contains(" ")) {
            throw new RuntimeException("Username cannot contain spaces");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new RuntimeException("Username can only contain letters, numbers, and underscores");
        }
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByPhonenumber(user.getPhonenumber())) {
            throw new RuntimeException("Phone number already exists");
        }

        user.setCreatedAt(LocalDateTime.now());
        user.setStatus("inactive");

        // First, save the new user
        UserEntity savedUser = userRepo.save(user);

        // 2. CALL THE NEW METHOD FROM CONTACT SERVICE
        // This will automatically qualify the lead if one exists with this email.
        try {
            contactService.qualifyLeadByEmail(savedUser.getEmail());
            System.out.println("Checked for lead conversion for email: " + savedUser.getEmail());
        } catch (Exception e) {
            // Log an error, but don't stop the registration process
            System.err.println("Error during lead qualification check: " + e.getMessage());
        }

        return savedUser;
    }

    public UserEntity loginUser(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, password);
    }

    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public UserEntity updateUser(Long id, UserEntity updatedUser) {
        Optional<UserEntity> existingUserOpt = userRepo.findById(id);
        if (existingUserOpt.isPresent()) {
            UserEntity existingUser = existingUserOpt.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhonenumber(updatedUser.getPhonenumber());
            existingUser.setLocation(updatedUser.getLocation());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            return userRepo.save(existingUser);
        } else {
            return null;
        }
    }

    public List<UserEntity> searchUsers(String keyword) {
        return userRepo.searchUsers(keyword);
    }

    public long countUsers() {
        return userRepo.count();
    }

    public Map<String, Object> getMonthlySignupData() {
        List<MonthlySignup> results = userRepo.findMonthlySignups();
        List<String> months = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        for (MonthlySignup signup : results) {
            months.add(signup.getMonth());
            counts.add(signup.getCount());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("months", months);
        response.put("counts", counts);

        return response;
    }
}