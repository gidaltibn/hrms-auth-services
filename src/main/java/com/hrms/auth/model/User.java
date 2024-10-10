package com.hrms.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

// A anotação @Entity indica que esta classe será mapeada para uma tabela no banco de dados
@Entity
@Getter // Lombok - Gera automaticamente os métodos getter para todos os campos
@Setter // Lombok - Gera automaticamente os métodos setter para todos os campos
public class User {

    // Anotação @Id define o campo 'id' como chave primária da tabela
    // Anotação @GeneratedValue define a estratégia de geração automática do valor da chave primária (IDENTITY significa que o banco de dados gerará um valor único)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotação @Column define as propriedades das colunas no banco de dados
    @Column(nullable = true) // O campo 'name' permite valores nulos
    private String name;

    @Column(unique = true, nullable = false) // O campo 'username' deve ser único e não pode ser nulo
    private String username;

    @Column(unique = true, nullable = false) // O campo 'email' deve ser único e não pode ser nulo
    private String email;

    @Column(unique = true, nullable = true) // O campo 'phone' deve ser único, mas pode ser nulo
    private String phone;

    @Column(unique = true, nullable = true) // O campo 'cpf' deve ser único, mas pode ser nulo
    private String cpf;

    @Column(unique = true, nullable = true) // O campo 'dataNascimento' deve ser único, mas pode ser nulo
    private String dataNascimento;

    @Column(unique = true, nullable = true) // O campo 'conselho' deve ser único, mas pode ser nulo
    private String conselho;

    @Column(nullable = false) // O campo 'password' não pode ser nulo
    private String password;

    // A anotação @ManyToMany indica um relacionamento muitos-para-muitos entre User e Role
    // fetch = FetchType.EAGER significa que as roles serão carregadas imediatamente junto com o usuário
    // A anotação @JoinTable define a tabela intermediária "user_roles" que associa 'user_id' e 'role_id'
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    // Métodos auxiliares para manipulação de roles (papéis) do usuário

    // Adiciona uma nova role ao conjunto de roles
    public void addRole(Role role) {
        roles.add(role);
    }

    // Remove uma role do conjunto de roles
    public void removeRole(Role role) {
        roles.remove(role);
    }

    // Remove todas as roles do usuário
    public void clearRoles() {
        roles.clear();
    }

    // Atualiza o conjunto de roles do usuário, removendo as atuais e adicionando a nova role fornecida
    public void updateRole(Role role) {
        roles.clear();
        roles.add(role);
    }

    // Verifica se o usuário possui uma role específica
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    // Verifica se o usuário possui uma role com base no nome da role
    public boolean hasRole(String roleName) {
        for (Role role : roles) {
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    // Verifica se o usuário possui pelo menos uma das roles fornecidas
    public boolean hasAnyRole(String... roleNames) {
        for (String roleName : roleNames) {
            if (hasRole(roleName)) {
                return true;
            }
        }
        return false;
    }

    // Métodos adicionais para manipulação de dados (getters e setters) - caso o Lombok não funcione
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getConselho() {
        return conselho;
    }

    public void setConselho(String conselho) {
        this.conselho = conselho;
    }

    public Long getId() {
        return id;
    }
}
