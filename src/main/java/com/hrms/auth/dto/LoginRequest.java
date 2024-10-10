package com.hrms.auth.dto;

// Classe DTO (Data Transfer Object) para o pedido de login
public class LoginRequest {

    // Armazena o nome de usuário do login
    private String username;

    // Armazena a senha do login
    private String password;

    // Getter para o nome de usuário
    public String getUsername() {
        return username;
    }

    // Setter para definir o nome de usuário
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter para a senha
    public String getPassword() {
        return password;
    }

    // Setter para definir a senha
    public void setPassword(String password) {
        this.password = password;
    }
}
