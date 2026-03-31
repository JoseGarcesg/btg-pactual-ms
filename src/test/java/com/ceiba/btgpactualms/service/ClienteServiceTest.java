package com.ceiba.btgpactualms.service;

import com.ceiba.btgpactualms.dto.SuscripcionRequest;
import com.ceiba.btgpactualms.exception.BusinessException;
import com.ceiba.btgpactualms.model.CanalNotificacion;
import com.ceiba.btgpactualms.model.Cliente;
import com.ceiba.btgpactualms.model.Fondo;
import com.ceiba.btgpactualms.model.FondoCliente;
import com.ceiba.btgpactualms.repository.ClienteRepository;
import com.ceiba.btgpactualms.repository.TransaccionRepository;
import com.ceiba.btgpactualms.service.notification.NotificacionFactory;
import com.ceiba.btgpactualms.service.notification.NotificacionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FondoService fondoService;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private NotificacionFactory notificacionFactory;

    @InjectMocks
    private ClienteService clienteService;

    // =========================
    // CASO 1: Cliente no existe
    // =========================
    @Test
    void deberiaLanzarErrorCuandoClienteNoExiste() {

        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("1");
        request.setFondoId(10);
        request.setMonto(50000.0);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> clienteService.suscribirse(request)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    // =========================
    // CASO 2: Fondo no existe
    // =========================
    @Test
    void deberiaLanzarErrorCuandoFondoNoExiste() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(100000.0);

        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("1");
        request.setFondoId(10);
        request.setMonto(50000.0);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        when(fondoService.getById(10))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> clienteService.suscribirse(request)
        );

        assertEquals("Fondo no existe", ex.getMessage());
    }

    // =========================
    // CASO 3: Saldo insuficiente
    // =========================
    @Test
    void deberiaFallarPorSaldoInsuficiente() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(10000.0);

        Fondo fondo = new Fondo();
        fondo.setId(10);
        fondo.setNombre("Fondo test");
        fondo.setMontoMinimo(50000.0);

        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("1");
        request.setFondoId(10);
        request.setMonto(50000.0);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        when(fondoService.getById(10))
                .thenReturn(Optional.of(fondo));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> clienteService.suscribirse(request)
        );
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().contains("No tiene saldo disponible para vincularse al fondo"));
    }

    // =========================
    // CASO 4: Suscripción exitosa
    // =========================
    @Test
    void deberiaSuscribirseCorrectamente() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(100000.0);
        cliente.setPreferenciaNotificacion(CanalNotificacion.EMAIL);
        cliente.setEmail("test@test.com");

        Fondo fondo = new Fondo();
        fondo.setId(10);
        fondo.setNombre("Fondo test");
        fondo.setMontoMinimo(10000.0);

        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("1");
        request.setFondoId(10);
        request.setMonto(20000.0);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        when(fondoService.getById(10))
                .thenReturn(Optional.of(fondo));

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        NotificacionService notificacionService = mock(NotificacionService.class);

        when(notificacionFactory.obtener(CanalNotificacion.EMAIL))
                .thenReturn(notificacionService);

        // ACT
        Cliente result = clienteService.suscribirse(request);

        // ASSERT
        assertNotNull(result);
        assertEquals(80000.0, result.getSaldo());

        verify(clienteRepository).save(any());
        verify(transaccionRepository).save(any());
        verify(notificacionService).enviar(anyString(), anyString());
    }

    // =========================
    // CASO 5: Cancelacion fallida cliente no existe
    // =========================
    @Test
    void deberiaFallarCuandoClienteNoExisteEnCancelacion() {

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.empty());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> clienteService.cancelarFondo("1", 10)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
    }

    // =========================
    // CASO 6: Cancelacion fallida cliente no tiene fondos suscritos
    // =========================
    @Test
    void deberiaFallarCuandoClienteNoTieneFondos() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setFondos(null);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> clienteService.cancelarFondo("1", 10)
        );

        assertEquals("El cliente no tiene fondos suscritos", ex.getMessage());
    }

    // =========================
    // CASO 7: Cancelacion fallida cliente no tiene dicho fondo
    // =========================
    @Test
    void deberiaFallarCuandoFondoNoEstaEnCliente() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(100000.0);

        FondoCliente fondoCliente = new FondoCliente();
        fondoCliente.setFondoId(999); // diferente al que buscas
        fondoCliente.setNombre("Otro fondo");
        fondoCliente.setMonto(10000.0);

        List<FondoCliente> fondos = new ArrayList<>();
        fondos.add(fondoCliente);

        cliente.setFondos(fondos);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> clienteService.cancelarFondo("1", 10)
        );

        assertEquals("Fondo no encontrado en el cliente", ex.getMessage());
    }

    // =========================
    // CASO 8: Cancelacion exitosa
    // =========================
    @Test
    void deberiaCancelarFondoCorrectamente() {

        // ===== CLIENTE =====
        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(100000.0);
        cliente.setPreferenciaNotificacion(CanalNotificacion.EMAIL);
        cliente.setEmail("test@test.com");

        // ===== FONDO CLIENTE =====
        FondoCliente fondoCliente = FondoCliente.builder()
                .fondoId(10)
                .nombre("Fondo test")
                .monto(20000.0)
                .build();

        cliente.setFondos(new java.util.ArrayList<>());
        cliente.getFondos().add(fondoCliente);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        NotificacionService notificacionService = mock(NotificacionService.class);

        when(notificacionFactory.obtener(CanalNotificacion.EMAIL))
                .thenReturn(notificacionService);

        when(clienteRepository.save(any()))
                .thenReturn(cliente);

        // ===== ACT =====
        Cliente result = clienteService.cancelarFondo("1", 10);

        // ===== ASSERT =====
        assertNotNull(result);
        assertEquals(120000.0, result.getSaldo()); // 100k + 20k

        assertEquals(0, result.getFondos().size());

        verify(clienteRepository).save(any());
        verify(transaccionRepository).save(any());
        verify(notificacionService).enviar(anyString(), anyString());
    }

    // =========================
    // CASO 9: Notifica por email
    // =========================
    @Test
    void deberiaEnviarNotificacionPorEmail() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(100000.0);
        cliente.setPreferenciaNotificacion(CanalNotificacion.EMAIL);
        cliente.setEmail("test@test.com");

        Fondo fondo = new Fondo();
        fondo.setId(10);
        fondo.setNombre("Fondo test");
        fondo.setMontoMinimo(10000.0);

        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("1");
        request.setFondoId(10);
        request.setMonto(20000.0);

        NotificacionService notificacionService = mock(NotificacionService.class);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        when(fondoService.getById(10))
                .thenReturn(Optional.of(fondo));

        when(clienteRepository.save(any()))
                .thenReturn(cliente);

        when(notificacionFactory.obtener(CanalNotificacion.EMAIL))
                .thenReturn(notificacionService);

        // ACT
        clienteService.suscribirse(request);

        // ASSERT
        verify(notificacionService).enviar(
                eq("Te has suscrito al fondo Fondo test"),
                eq("test@test.com")
        );
    }

    // =========================
    // CASO 10: Notifica por sms
    // =========================
    @Test
    void deberiaEnviarNotificacionPorSms() {

        Cliente cliente = new Cliente();
        cliente.setId("1");
        cliente.setSaldo(100000.0);
        cliente.setPreferenciaNotificacion(CanalNotificacion.SMS);
        cliente.setTelefono("3001234567");

        Fondo fondo = new Fondo();
        fondo.setId(10);
        fondo.setNombre("Fondo test");
        fondo.setMontoMinimo(10000.0);

        SuscripcionRequest request = new SuscripcionRequest();
        request.setClienteId("1");
        request.setFondoId(10);
        request.setMonto(20000.0);

        NotificacionService notificacionService = mock(NotificacionService.class);

        when(clienteRepository.findById("1"))
                .thenReturn(Optional.of(cliente));

        when(fondoService.getById(10))
                .thenReturn(Optional.of(fondo));

        when(clienteRepository.save(any()))
                .thenReturn(cliente);

        when(notificacionFactory.obtener(CanalNotificacion.SMS))
                .thenReturn(notificacionService);

        // ACT
        clienteService.suscribirse(request);

        // ASSERT
        verify(notificacionService).enviar(
                eq("Te has suscrito al fondo Fondo test"),
                eq("3001234567")
        );
    }
}
