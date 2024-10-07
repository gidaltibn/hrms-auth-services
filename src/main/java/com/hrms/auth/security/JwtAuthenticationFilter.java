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

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenUtil jwtTokenUtil;
    private UserService userService; // Garantindo que o UserService esteja disponível

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserService userService, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService; // Injeção do UserService
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String jwt = getJwtFromRequest(request);

        if (jwt != null && jwtTokenUtil.validateToken(jwt, jwtTokenUtil.getUsernameFromToken(jwt))) {
            String username = jwtTokenUtil.getUsernameFromToken(jwt);
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " do token
        }
        return null;
    }
}
