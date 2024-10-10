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
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    // Endpoint para autenticar o usuário e gerar o token JWT
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        System.out.println("Roles: " + user.getRoles());
        try {
            userService.createUser(user);
            return ResponseEntity.ok("Usuário criado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Tentar autenticar o usuário com base nas credenciais fornecidas
            System.out.println("\nUsername: " + loginRequest.getUsername() + " Password: " + loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Colocar as informações do usuário autenticado no contexto de segurança do Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obter os detalhes do usuário autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Gerar o token JWT para o usuário autenticado
            String token = jwtTokenUtil.generateToken(userDetails);
            System.out.println("\nToken: " + token);

            // Retornar a resposta contendo o token JWT
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            // Capturar exceções de autenticação e retornar uma resposta apropriada
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    // Endpoint para logout (opcional, pode ser apenas informativo no caso de JWT)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // Aqui, como JWT é stateless, o logout pode ser uma resposta informativa
        return ResponseEntity.ok("Logout realizado com sucesso.");
    }
}
