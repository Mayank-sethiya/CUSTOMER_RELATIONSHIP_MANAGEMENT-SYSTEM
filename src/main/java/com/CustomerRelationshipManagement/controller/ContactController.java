package com.CustomerRelationshipManagement.controller;

import com.CustomerRelationshipManagement.entity.ContactEntity;
import com.CustomerRelationshipManagement.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/support")
public class ContactController {

    @Autowired
    private ContactService service;


    @GetMapping
    public List<ContactEntity> getAll() {
        return service.getAllContacts();
    }

    @PutMapping("/{id}")
    public ContactEntity updateStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateStatus(id, status);
    }
    @PostMapping
    public ContactEntity createLead(@RequestBody ContactEntity lead) {
        return service.saveLead(lead);
    }
    @GetMapping("/count")
    public long getTotalLeads() {
        return service.getTotalLeadsCount();
    }



}
