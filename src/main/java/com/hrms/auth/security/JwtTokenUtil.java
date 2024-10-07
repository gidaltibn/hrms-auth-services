package com.hrms.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private final String jwtSecret = System.getenv("JWT_SECRET");
    private final int jwtExpirationMs = 3600000; // 1 hora

    //criar um método para gerar e retornar um token JWT com base nos detalhes do usuário
    public String generateToken(UserDetails userDetails) {
        System.out.println(jwtSecret);
        try {
            String token = Jwts.builder()
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
            System.out.println("Generated Token: " + token);
            return token;
        } catch (Exception e) {
            System.err.println("Error generating token: " + e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }


    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
        return expiration.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // Aqui estamos retornando o "subject", que é o username
    }



}