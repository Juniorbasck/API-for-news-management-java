package com.newsapi.api_demo.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class UserModel {
    @Id
    private String id;
    private String nome;
    private String senha;
    private String email;
    private Boolean isAdmin;
}
