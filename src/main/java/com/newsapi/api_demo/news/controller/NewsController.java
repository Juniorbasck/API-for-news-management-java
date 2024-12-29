package com.newsapi.api_demo.news.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsapi.api_demo.news.services.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewService service;

    @GetMapping
    public ResponseEntity<?> getAllNews() {
        try {
            JsonNode news = service.getAllNews();
            return ResponseEntity.ok(news);
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(
                    String.format("Error occurred while retrieving news. Error: %s", e.getMessage())
            );
        }
    }

    @PutMapping("/like")
    public ResponseEntity<?> likeNews(@RequestParam("id") String newsId) {
        try {
            service.likeNews(newsId);
            return ResponseEntity.ok(
                    Map.of("status", 200, "message", "Curtida registrada com sucesso!")
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("status", 500, "message", "Erro ao registrar curtida", "error", e.getMessage())
            );
        }
    }
}
