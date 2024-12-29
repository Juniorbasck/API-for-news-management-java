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

    @PutMapping("/edit")
    public ResponseEntity<?> editNews(
            @RequestParam("id") String newsId,
            @RequestParam("userId") String userId,
            @RequestBody Map<String, Object> updateData) {
        try {
            service.editNews(userId, newsId, updateData);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Notícia atualizada com sucesso!"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", 403,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", 500,
                    "message", "Erro ao atualizar a notícia",
                    "error", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNews(
            @RequestParam("userId") String userId,
            @RequestParam("newsId") String newsId) {
        try {
            service.deleteNews(userId, newsId);
            return ResponseEntity.ok(Map.of(
                    "status", 200,
                    "message", "Notícia deletada com sucesso!"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of(
                    "status", 400,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", 500,
                    "message", "Erro ao deletar a notícia",
                    "error", e.getMessage()
            ));
        }
    }
}
