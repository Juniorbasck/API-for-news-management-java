package com.newsapi.api_demo.user.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.Map;

@Service
public class UserService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.api-key}")
    private String supabaseApiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public Map<String, Object> login(String email, String senha) {
        try {
            String url = supabaseUrl + "/usuarios?email=eq." + email + "&senha=eq." + senha;

            System.out.println("URL da requisição: " + url);
            System.out.println("Supabase API Key: " + supabaseApiKey);

            // Configuração dos cabeçalhos
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseApiKey);
            headers.set("apikey", supabaseApiKey);

            System.out.println("Cabeçalhos da requisição: " + headers);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                List<Map<String, Object>> users = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );

                if (users.isEmpty()) {
                    throw new IllegalArgumentException("E-mail ou senha inválidos");
                }

                // Retorna o primeiro usuário encontrado
                return users.get(0);
            } else {
                throw new IllegalArgumentException("Erro ao autenticar usuário: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
    }

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
