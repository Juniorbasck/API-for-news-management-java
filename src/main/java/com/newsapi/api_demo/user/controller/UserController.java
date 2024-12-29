package com.newsapi.api_demo.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsapi.api_demo.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String senha = requestBody.get("senha");

        if (email == null || senha == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", 400,
                    "message", "E-mail e senha são obrigatórios"
            ));
        }

        try {
            Map<String, Object> user = userService.login(email, senha);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Usuário autenticado com sucesso",
                    "data", user
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", 401,
                    "message", e.getMessage()
            ));
        }
    }

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
