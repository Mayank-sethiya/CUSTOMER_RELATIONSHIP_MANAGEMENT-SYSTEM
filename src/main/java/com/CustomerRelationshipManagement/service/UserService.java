package com.CustomerRelationshipManagement.service;

import com.CustomerRelationshipManagement.entity.UserEntity;
import com.CustomerRelationshipManagement.repository.MonthlySignup; // NEW IMPORT
import com.CustomerRelationshipManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList; // NEW IMPORT
import java.util.HashMap;   // NEW IMPORT
import java.util.List;      // NEW IMPORT
import java.util.Map;       // NEW IMPORT
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public UserEntity registerUser(UserEntity user) {
        String email = user.getEmail();
        String username = user.getUsername();

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

        return userRepo.save(user);
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

    /**
     * THIS IS THE CORRECTED METHOD.
     * It now calls the efficient native query from your UserRepository.
     */
    public Map<String, Object> getMonthlySignupData() {
        // 1. Call the NEW, efficient repository method
        List<MonthlySignup> results = userRepo.findMonthlySignups();

        // 2. Prepare lists for the response
        List<String> months = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        // 3. Loop through the clean results from the database
        for (MonthlySignup signup : results) {
            months.add(signup.getMonth());
            counts.add(signup.getCount());
        }

        // 4. Create the final map object to send as JSON
        Map<String, Object> response = new HashMap<>();
        response.put("months", months);
        response.put("counts", counts);

        return response;
    }
}