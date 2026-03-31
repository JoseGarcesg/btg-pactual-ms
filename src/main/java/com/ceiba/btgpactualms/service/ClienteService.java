package com.ceiba.btgpactualms.service;

import com.ceiba.btgpactualms.dto.SuscripcionRequest;
import com.ceiba.btgpactualms.exception.BusinessException;
import com.ceiba.btgpactualms.model.*;
import com.ceiba.btgpactualms.repository.ClienteRepository;
import com.ceiba.btgpactualms.repository.TransaccionRepository;
import com.ceiba.btgpactualms.service.notification.NotificacionFactory;
import com.ceiba.btgpactualms.service.notification.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final FondoService fondoService;
    private final TransaccionRepository transaccionRepository;
    private final NotificacionFactory notificacionFactory;

    public Cliente suscribirse(SuscripcionRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        Fondo fondo = fondoService.getById(request.getFondoId())
                .orElseThrow(() -> new BusinessException("Fondo no existe"));

        if (cliente.getSaldo() < fondo.getMontoMinimo()) {
            throw new BusinessException(
                    "No tiene saldo disponible para vincularse al fondo " + fondo.getNombre()
            );
        }

        if (request.getMonto() < fondo.getMontoMinimo()) {
            throw new BusinessException(
                    "El monto mínimo para este fondo es: " + fondo.getMontoMinimo()
            );
        }

        if (cliente.getSaldo() < request.getMonto()) {
            throw new BusinessException(
                    "Saldo insuficiente para realizar la suscripción");
        }

        // Inicializar lista si es null
        if (cliente.getFondos() == null) {
            cliente.setFondos(new ArrayList<>());
        }

        // Crear fondo cliente
        FondoCliente fondoCliente = FondoCliente.builder()
                .fondoId(fondo.getId())
                .nombre(fondo.getNombre())
                .monto(request.getMonto())
                .fechaApertura(LocalDate.now())
                .build();

        // Restar saldo
        cliente.setSaldo(cliente.getSaldo() - request.getMonto());

        // Agregar fondo
        cliente.getFondos().add(fondoCliente);

        String mensaje = "Te has suscrito al fondo " + fondo.getNombre();

        enviarNotificacion(cliente, mensaje);

        // Agregar al historial de transacciones
        Transaccion tx = Transaccion.builder()
                .id(java.util.UUID.randomUUID().toString())
                .clienteId(cliente.getId())
                .fondoId(fondo.getId())
                .tipo(TipoTransaccion.APERTURA)
                .monto(request.getMonto())
                .fecha(java.time.LocalDateTime.now())
                .build();

        transaccionRepository.save(tx);

        return clienteRepository.save(cliente);
    }

    public Cliente cancelarFondo(String clienteId, Integer fondoId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        if (cliente.getFondos() == null || cliente.getFondos().isEmpty()) {
            throw new BusinessException("El cliente no tiene fondos suscritos");
        }

        FondoCliente fondo = cliente.getFondos().stream()
                .filter(f -> f.getFondoId().equals(fondoId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Fondo no encontrado en el cliente"));

        // 🔥 DEVOLVER DINERO
        cliente.setSaldo(cliente.getSaldo() + fondo.getMonto());

        // 🔥 ELIMINAR FONDO
        cliente.getFondos().remove(fondo);

        // Agregar al historial de transacciones
        Transaccion tx = Transaccion.builder()
                .id(java.util.UUID.randomUUID().toString())
                .clienteId(cliente.getId())
                .fondoId(fondo.getFondoId())
                .tipo(TipoTransaccion.CANCELACION)
                .monto(fondo.getMonto())
                .fecha(java.time.LocalDateTime.now())
                .build();

        String mensaje = "Has cancelado el fondo " + fondo.getNombre();

        enviarNotificacion(cliente, mensaje);

        transaccionRepository.save(tx);

        return clienteRepository.save(cliente);
    }

    public List<Transaccion> historial(String clienteId) {
        // 🔥 VALIDAR CLIENTE
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));
        return transaccionRepository.findByClienteId(clienteId);
    }

    private void enviarNotificacion(Cliente cliente, String mensaje) {

        NotificacionService notificacionService =
                notificacionFactory.obtener(cliente.getPreferenciaNotificacion());

        System.out.println("enviando Notificacion ");

        if (notificacionService == null) return;

        String destino = cliente.getPreferenciaNotificacion().equals(CanalNotificacion.EMAIL)
                ? cliente.getEmail()
                : cliente.getTelefono();


        System.out.println("destino: " + destino);
        notificacionService.enviar(mensaje, destino);
    }


}
