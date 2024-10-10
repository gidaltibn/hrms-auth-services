package com.hrms.auth.dto;

// Classe simples de resposta JWT (JSON Web Token)
public class JwtResponse {

    // Armazena o token JWT
    private String token;

    // Construtor para inicializar o token
    public JwtResponse(String token) {
        this.token = token;
    }

    // Getter para acessar o valor do token
    public String getToken() {
        return token;
    }
}
