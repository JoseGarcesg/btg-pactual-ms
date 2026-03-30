package com.ceiba.btgpactualms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // desactivar csrf (para pruebas)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // login libre
                        .anyRequest().authenticated() // todo lo demás protegido
                );

        return http.build();
    }
}
