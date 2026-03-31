package com.ceiba.btgpactualms.service;

import com.ceiba.btgpactualms.exception.BusinessException;
import com.ceiba.btgpactualms.model.Usuario;
import com.ceiba.btgpactualms.repository.UsuarioRepository;
import com.ceiba.btgpactualms.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @Mock
    private UsuarioRepository repository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deberiaLoginCorrectoYGenerarToken() {

        Usuario usuario = new Usuario();
        usuario.setUsername("jose");
        usuario.setPassword("1234");
        usuario.setRol("ADMIN");

        when(repository.findByUsername("jose"))
                .thenReturn(Optional.of(usuario));

        when(jwtService.generarToken("jose", "ADMIN"))
                .thenReturn("token-123");

        String token = usuarioService.login("jose", "1234");

        assertEquals("token-123", token);

        verify(jwtService).generarToken("jose", "ADMIN");
    }

    @Test
    void deberiaFallarCuandoUsuarioNoExiste() {

        when(repository.findByUsername("jose"))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> usuarioService.login("jose", "1234")
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void deberiaFallarCuandoPasswordEsIncorrecto() {

        Usuario usuario = new Usuario();
        usuario.setUsername("jose");
        usuario.setPassword("1234");

        when(repository.findByUsername("jose"))
                .thenReturn(Optional.of(usuario));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> usuarioService.login("jose", "9999")
        );

        assertEquals("Credenciales inválidas", ex.getMessage());
    }

    @Test
    void deberiaCrearUsuarioCorrectamente() {

        Usuario usuario = new Usuario();
        usuario.setUsername("jose");
        usuario.setPassword("1234");

        when(repository.findByUsername("jose"))
                .thenReturn(Optional.empty());

        when(repository.save(usuario))
                .thenReturn(usuario);

        Usuario result = usuarioService.crearUsuario(usuario);

        assertEquals("jose", result.getUsername());

        verify(repository).save(usuario);
    }

    @Test
    void deberiaFallarCuandoUsuarioYaExiste() {

        Usuario usuario = new Usuario();
        usuario.setUsername("jose");

        when(repository.findByUsername("jose"))
                .thenReturn(Optional.of(usuario));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> usuarioService.crearUsuario(usuario)
        );

        assertEquals("El usuario ya existe", ex.getMessage());

        verify(repository, never()).save(any());
    }
}
