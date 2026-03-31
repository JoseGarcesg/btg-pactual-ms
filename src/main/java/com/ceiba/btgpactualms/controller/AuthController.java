package com.ceiba.btgpactualms.controller;

import com.ceiba.btgpactualms.exception.BusinessException;
import com.ceiba.btgpactualms.model.Usuario;
import com.ceiba.btgpactualms.repository.UsuarioRepository;
import com.ceiba.btgpactualms.security.JwtService;
import com.ceiba.btgpactualms.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public String login(@RequestBody Usuario request) {
        return usuarioService.login(
                request.getUsername(),
                request.getPassword()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.crearUsuario(usuario));
    }
}
