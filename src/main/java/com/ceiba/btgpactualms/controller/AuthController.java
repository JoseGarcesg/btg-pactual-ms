package com.ceiba.btgpactualms.controller;

import com.ceiba.btgpactualms.exception.BusinessException;
import com.ceiba.btgpactualms.model.Usuario;
import com.ceiba.btgpactualms.repository.UsuarioRepository;
import com.ceiba.btgpactualms.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody Usuario request) {

        // 🔍 Buscar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        // 🔐 Validar contraseña
        if (!usuario.getPassword().equals(request.getPassword())) {
            throw new BusinessException("Credenciales inválidas");
        }

        // 🎯 Generar token
        return jwtService.generarToken(usuario.getUsername());
    }

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario request) {

        // 🔍 Validar si ya existe
        usuarioRepository.findByUsername(request.getUsername())
                .ifPresent(u -> {
                    throw new BusinessException("El usuario ya existe");
                });

        // 🎯 Guardar usuario
        return usuarioRepository.save(request);
    }
}
