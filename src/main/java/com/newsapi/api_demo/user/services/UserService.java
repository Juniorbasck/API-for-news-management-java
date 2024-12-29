package com.newsapi.api_demo.user.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api-key}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode getAllUsers() {
        try {
            String url = supabaseUrl + "/usuarios";

            System.out.println("URL da requisição: " + url);
            System.out.println("Supabase API Key: " + supabaseApiKey);

            // Configuração dos cabeçalhos
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.set("apikey", supabaseApiKey);

            System.out.println("Cabeçalhos da requisição: " + headers);

            org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    String.class
            );

            // Retorno como JSON
            return objectMapper.readTree(response.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuários: " + e.getMessage(), e);
        }
    }
}
