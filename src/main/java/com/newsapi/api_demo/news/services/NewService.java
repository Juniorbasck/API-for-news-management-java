package com.newsapi.api_demo.news.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class NewService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api-key}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getAllNews() {
        try {
            String url = supabaseUrl + "/noticias";

            // Configurar os cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.set("apikey", supabaseApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Fazer a requisição GET
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Retornar os dados como JSON
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar notícias: " + e.getMessage(), e);
        }
    }

    public void likeNews(String newsId) {
        try {
            // Etapa 1: Buscar a notícia existente
            String getUrl = supabaseUrl + "/noticias?id=eq." + newsId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.set("apikey", supabaseApiKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            System.out.println("GET URL: " + getUrl);

            ResponseEntity<String> response = restTemplate.exchange(
                    getUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JsonNode responseBody = objectMapper.readTree(response.getBody());
            if (responseBody.isEmpty()) {
                throw new IllegalArgumentException("Notícia não encontrada");
            }

            // Extrair os campos existentes da notícia
            JsonNode news = responseBody.get(0);
            String title = news.get("title").asText();
            String url = news.get("url").asText();
            int currentLikes = news.has("likes") ? news.get("likes").asInt() : 0;
            int updatedLikes = currentLikes + 1;

            // Etapa 2: Atualizar os dados com PUT
            String putUrl = supabaseUrl + "/noticias?id=eq." + newsId;

            Map<String, Object> body = Map.of(
                    "id", newsId,
                    "title", title,
                    "url", url,
                    "likes", updatedLikes
            );

            System.out.println("PUT URL: " + putUrl);
            System.out.println("PUT Body: " + objectMapper.writeValueAsString(body));

            HttpEntity<String> putEntity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            ResponseEntity<Void> putResponse = restTemplate.exchange(
                    putUrl,
                    HttpMethod.PUT,
                    putEntity,
                    Void.class
            );

            if (!putResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Erro ao registrar curtida");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao curtir notícia: " + e.getMessage(), e);
        }
    }
}
