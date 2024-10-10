package com.hrms.auth.dto;

import java.util.Collection;

// Classe DTO (Data Transfer Object) para o pedido de registro (signup)
public class SignupRequest {

    // Armazena o nome de usuário do novo registro
    private String username;

    // Armazena a senha do novo registro
    private String password;

    // Armazena uma coleção de papéis (roles) que serão atribuídos ao novo usuário
    private Collection<String> roles;

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

    // Getter para a coleção de papéis (roles)
    public Collection<String> getRoles() {
        return roles;
    }

    // Setter para definir a coleção de papéis (roles)
    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
}
