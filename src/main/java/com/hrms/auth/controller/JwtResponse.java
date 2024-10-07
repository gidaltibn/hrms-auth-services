package com.hrms.auth.controller;

public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }
}