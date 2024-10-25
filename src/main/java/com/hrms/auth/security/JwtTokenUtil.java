package com.hrms.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private final String jwtSecret = System.getenv("JWT_SECRET"); // Secret key para assinar o JWT, obtida das variáveis de ambiente
    private final int jwtExpirationMs = 3600000; // Duração do token JWT, aqui configurada para 1 hora (em milissegundos)

    // Método para gerar e retornar um token JWT com base nos detalhes do usuário (username)
    public String generateToken(UserDetails userDetails) {
        try {
            // Constrói o JWT com o nome de usuário como "subject", data de emissão e data de expiração, e assina usando a chave secreta e o algoritmo HS512
            String token = Jwts.builder()
                    .setSubject(userDetails.getUsername())  // Define o nome de usuário como "subject" do token
                    .claim("roles", userDetails.getAuthorities())  // Adiciona as permissões (roles) do usuário ao token
                    .setIssuedAt(new Date())  // Data de criação do token
                    .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))  // Define a data de expiração do token
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)  // Assina o token com a chave secreta
                    .compact();  // Gera o token final
            return token;  // Retorna o token JWT
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());  // Captura e exibe erros durante a geração do token
            return null;  // Retorna null caso ocorra algum erro
        }
    }

    // Método para validar o token JWT. Verifica se o nome de usuário no token corresponde ao esperado e se o token não está expirado
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);  // Extrai o nome de usuário do token
        return (extractedUsername.equals(username) && !isTokenExpired(token));  // Retorna verdadeiro se o nome de usuário for igual e o token não estiver expirado
    }

    // Método para extrair o nome de usuário do token JWT
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();  // Extrai o "subject" (nome de usuário) do corpo do token
    }

    // Método para verificar se o token está expirado
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();  // Extrai a data de expiração do token
        return expiration.before(new Date());  // Verifica se a data de expiração é anterior à data atual
    }

    // Método auxiliar para obter o nome de usuário diretamente a partir do token JWT
    public String getUsernameFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Retorna o "subject" (nome de usuário) a partir do token

        return username;  // Retorna o nome de usuário extraído do token
    }
}
