package com.ceiba.btgpactualms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("ENTRA AL FILTRO");

        // 🔍 Validar header
        if (header == null || !header.toLowerCase().startsWith("bearer ")) {
            filterChain.doFilter(request, response);
            System.out.println("ENTRA a Validar header " + header);
            return;
        }

        String token = header.substring(7);

        try {
            String username = jwtService.extraerUsername(token);
            String rol = jwtService.extraerRol(token);
            System.out.println("ROL: " + rol);

            if (username != null) {

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                List.of(new SimpleGrantedAuthority("ROLE_" + rol))
                        );

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            System.out.println("ERROR EN TOKEN: " + e.getMessage());
            e.printStackTrace(); // 🔥 CLAVE
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
