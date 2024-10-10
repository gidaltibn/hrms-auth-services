package com.hrms.auth.controller;

import com.hrms.auth.dto.JwtResponse;
import com.hrms.auth.dto.LoginRequest;
import com.hrms.auth.model.User;
import com.hrms.auth.security.JwtTokenUtil;
import com.hrms.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;  // Gerencia a autenticação de usuários

    @Autowired
    private JwtTokenUtil jwtTokenUtil;  // Utilitário para geração e validação de tokens JWT

    @Autowired
    private UserService userService;  // Serviço que lida com operações relacionadas ao usuário

    // Endpoint para o cadastro de novos usuários
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        // Log para mostrar as roles recebidas
        System.out.println("Roles: " + user.getRoles());
        try {
            // Chama o serviço para criar um novo usuário
            userService.createUser(user);
            return ResponseEntity.ok("Usuário criado com sucesso!");
        } catch (IllegalArgumentException e) {
            // Retorna uma resposta de erro se o nome de usuário já estiver em uso
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para login e geração de token JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Tenta autenticar o usuário com o nome de usuário e senha fornecidos
            System.out.println("\nUsername: " + loginRequest.getUsername() + " Password: " + loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Coloca a autenticação no contexto de segurança do Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obter os detalhes do usuário autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Gera o token JWT com base nas informações do usuário autenticado
            String token = jwtTokenUtil.generateToken(userDetails);
            System.out.println("\nToken: " + token);

            // Retorna a resposta com o token gerado
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            // Captura exceções de autenticação e retorna uma resposta de erro
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    // Endpoint para logout (informativo, já que o JWT é stateless)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // JWT não tem um estado de sessão, então este logout é puramente informativo
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }
}
