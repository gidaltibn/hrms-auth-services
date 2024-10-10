package com.hrms.auth.security;

import com.hrms.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration  // Marca esta classe como uma classe de configuração do Spring.
@EnableWebSecurity  // Ativa a segurança web no Spring.
public class SecurityConfig {

    @Autowired
    @Lazy  // Injeta o UserService e carrega-o de forma preguiçosa para evitar dependências circulares.
    private UserService userService;

    // Configuração do AuthenticationManagerBuilder para usar o userDetailsService fornecido pelo UserService.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);  // Autentica os usuários usando o UserService.
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;  // Injeta o utilitário JwtTokenUtil para manipulação de tokens JWT.

    // Define a configuração da cadeia de filtros de segurança.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desativa a proteção CSRF, já que o JWT é stateless.
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()  // Permite acesso livre aos endpoints que começam com "/auth".
                        .anyRequest().authenticated()  // Qualquer outra requisição precisa ser autenticada.
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Configura o gerenciamento de sessão para ser stateless, já que JWT não mantém sessões no servidor.
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);  // Adiciona o filtro JwtAuthenticationFilter antes do UsernamePasswordAuthenticationFilter.

        return http.build();  // Constroi a configuração de segurança.
    }

    // Define o AuthenticationManager necessário para autenticar usuários.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();  // Retorna o AuthenticationManager configurado.
    }

    // Define o PasswordEncoder que será usado para codificar e comparar senhas.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usa BCrypt para codificar senhas.
    }

    // Define o filtro JwtAuthenticationFilter, que vai interceptar e validar tokens JWT nas requisições.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        // Passa o JwtTokenUtil, o UserService e o AuthenticationManager para o filtro.
        return new JwtAuthenticationFilter(jwtTokenUtil, userService, authenticationManager(null));
    }
}
