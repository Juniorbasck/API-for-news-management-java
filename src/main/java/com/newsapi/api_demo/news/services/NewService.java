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
}
