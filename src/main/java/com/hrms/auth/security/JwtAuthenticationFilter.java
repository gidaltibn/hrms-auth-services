package com.hrms.auth.security;

import com.hrms.auth.service.UserService;
import jakarta.servlet.ServletException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filtro para autenticação JWT que será executado em todas as requisições
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;  // Utilitário para manipular tokens JWT
    private UserService userService;    // Serviço de usuário para carregar os detalhes do usuário

    private final AuthenticationManager authenticationManager;

    // Construtor injetando dependências de JwtTokenUtil, UserService e AuthenticationManager
    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService; // Injeção do UserService
        this.authenticationManager = authenticationManager;
    }

    // Método principal do filtro que é chamado em cada requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Recupera o token JWT da requisição
        String jwt = getJwtFromRequest(request);

        // Se o token for válido, extrai o nome de usuário e carrega os detalhes do usuário
        if (jwt != null && jwtTokenUtil.validateToken(jwt, jwtTokenUtil.getUsernameFromToken(jwt))) {
            String username = jwtTokenUtil.getUsernameFromToken(jwt);
            UserDetails userDetails = userService.loadUserByUsername(username); // Carrega os detalhes do usuário

            // Se o usuário for encontrado, configura o contexto de autenticação do Spring
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication); // Define a autenticação no contexto de segurança
            }
        }
        // Continua a cadeia de filtros
        chain.doFilter(request, response);
    }

    // Método auxiliar para extrair o token JWT do cabeçalho da requisição
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");  // Recupera o cabeçalho "Authorization"
        // Verifica se o token começa com "Bearer "
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " do token
        }
        return null;
    }
}
