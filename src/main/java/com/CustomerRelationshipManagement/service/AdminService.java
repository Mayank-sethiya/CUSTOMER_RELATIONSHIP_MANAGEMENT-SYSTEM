package com.CustomerRelationshipManagement.service;

import com.CustomerRelationshipManagement.entity.AdminEntity;
import com.CustomerRelationshipManagement.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository userRepo;

    public AdminEntity register(AdminEntity user) {
        return userRepo.save(user);
    }

    public AdminEntity login(String username, String password) {
        AdminEntity user = userRepo.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
