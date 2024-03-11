package com.SmartContactManager.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.SmartContactManager.smart.dao.ContactRepository;
import com.SmartContactManager.smart.dao.UserRepository;
import com.SmartContactManager.smart.entities.Contact;
import com.SmartContactManager.smart.entities.User;

@RestController
public class SearchController {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    //search handeller
   @GetMapping("/search/{query}")
    public ResponseEntity<?> search( @PathVariable("query") String query, Principal principal ){

        User user = this.userRepository.getUserByUserName(principal.getName());
       List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
      
       return ResponseEntity.ok(contacts);
        
    }
    
}
