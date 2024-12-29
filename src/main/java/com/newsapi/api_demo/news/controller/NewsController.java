package com.newsapi.api_demo.news.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.newsapi.api_demo.news.services.NewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
