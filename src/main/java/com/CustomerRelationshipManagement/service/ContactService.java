package com.CustomerRelationshipManagement.service;

import com.CustomerRelationshipManagement.entity.ContactEntity;
import com.CustomerRelationshipManagement.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime; // 1. Make sure this import is present
import java.util.List;

@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public List<ContactEntity> getAllContacts() {
        return repository.findAll();
    }

    public ContactEntity updateStatus(Long id, String newStatus) {
        ContactEntity contact = repository.findById(id).orElse(null);
        if (contact != null) {
            contact.setStatus(newStatus);
            return repository.save(contact);
        }
        return null;
    }

    public ContactEntity saveLead(ContactEntity lead) {
        // 2. Add this line to set the current timestamp on the new lead
        lead.setCreatedAt(LocalDateTime.now());

        // 3. Now save the lead
        return repository.save(lead);
    }

    public ContactEntity qualifyLeadByEmail(String email) {
        ContactEntity lead = repository.findByEmail(email);
        if (lead != null) {
            lead.setStatus("qualified");
            return repository.save(lead);
        }
        return null;
    }

    public long getTotalLeadsCount() {
        return repository.count();
    }
}