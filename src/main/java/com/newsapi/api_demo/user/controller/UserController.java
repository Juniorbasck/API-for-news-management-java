package com.newsapi.api_demo.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsapi.api_demo.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<JsonNode> getAllUsers() {
        System.out.println("Endpoint '/users' foi acessado.");
        try {
            JsonNode users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
