package com.ceiba.btgpactualms.security;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;

import java.util.Date;

@Service
public class JwtService {
    // 🔐 CLAVE SEGURA (mínimo 256 bits)
    private final String SECRET = "my-super-secret-key-my-super-secret-key";

    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)// ✅ usar setSubject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET.getBytes()) // ✅ así
                .compact();
    }

    public String extraerUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes()) // ✅ así
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extraerRol(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }
}
