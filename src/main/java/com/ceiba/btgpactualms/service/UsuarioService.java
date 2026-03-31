package com.ceiba.btgpactualms.service;

import com.ceiba.btgpactualms.exception.BusinessException;
import com.ceiba.btgpactualms.model.Usuario;
import com.ceiba.btgpactualms.repository.UsuarioRepository;
import com.ceiba.btgpactualms.security.JwtService;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository repository,
                          JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    public String login(String username, String password) {

        // 🔍 Buscar usuario
        Usuario usuario = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        // 🔐 Validar contraseña
        if (!usuario.getPassword().equals(password)) {
            throw new BusinessException("Credenciales inválidas");
        }

        // 🎯 Generar token
        return jwtService.generarToken(usuario.getUsername(), usuario.getRol());
    }

    public Usuario crearUsuario(Usuario usuario) {

        // 🔥 VALIDACIÓN CLAVE
        if (repository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new BusinessException("El usuario ya existe");
        }

        return repository.save(usuario);
    }
}
