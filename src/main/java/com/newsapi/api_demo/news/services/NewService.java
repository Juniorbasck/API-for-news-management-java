package com.newsapi.api_demo.news.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

            JsonNode news = responseBody.get(0);
            String title = news.get("title").asText();
            String url = news.get("url").asText();
            int currentLikes = news.has("likes") ? news.get("likes").asInt() : 0;
            int updatedLikes = currentLikes + 1;

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

    public void editNews(String userId, String newsId, Map<String, Object> updateData) {
        try {
            // 1. Verificar se o usuário é admin
            String userUrl = supabaseUrl + "/usuarios?id=eq." + userId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.set("apikey", supabaseApiKey);

            ResponseEntity<String> userResponse = restTemplate.exchange(
                    userUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            JsonNode userBody = objectMapper.readTree(userResponse.getBody());
            JsonNode userNode = userBody.get(0); // Pega o primeiro usuário
            if (!userNode.get("isadmin").asBoolean()) {
                throw new IllegalArgumentException("Usuário não possui permissão para editar notícias");
            }

            // 2. Buscar a notícia para verificar se existe
            String newsUrl = supabaseUrl + "/noticias?id=eq." + newsId;
            ResponseEntity<String> newsResponse = restTemplate.exchange(
                    newsUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

            JsonNode newsBody = objectMapper.readTree(newsResponse.getBody());
            if (newsBody.isEmpty()) {
                throw new IllegalArgumentException("Notícia não encontrada");
            }

            // 3. Atualizar a notícia
            Map<String, Object> validFields = updateData.entrySet().stream()
                    .filter(entry -> List.of("title", "abstract", "url", "published_date").contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            validFields.put("id", newsId); // Inclua o ID no corpo da requisição

            headers.setContentType(MediaType.APPLICATION_JSON); // Define Content-Type como JSON
            System.out.println("Campos válidos enviados ao Supabase: " + objectMapper.writeValueAsString(validFields));

            HttpEntity<String> updateEntity = new HttpEntity<>(
                    objectMapper.writeValueAsString(validFields), headers);
            ResponseEntity<Void> updateResponse = restTemplate.exchange(
                    newsUrl, HttpMethod.PUT, updateEntity, Void.class);

            if (!updateResponse.getStatusCode().is2xxSuccessful()) {
                throw new IllegalArgumentException("Erro ao atualizar a notícia");
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao editar notícia: " + e.getMessage(), e);
        }
    }
}
