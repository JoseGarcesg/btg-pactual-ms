package com.ceiba.btgpactualms.service;

import com.ceiba.btgpactualms.dto.SuscripcionRequest;
import com.ceiba.btgpactualms.model.Cliente;
import com.ceiba.btgpactualms.model.Fondo;
import com.ceiba.btgpactualms.model.FondoCliente;
import com.ceiba.btgpactualms.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final FondoService fondoService;

    public Cliente suscribirse(String clienteId, SuscripcionRequest request) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Fondo fondo = fondoService.getById(request.getFondoId())
                .orElseThrow(() -> new RuntimeException("Fondo no existe"));

        if (cliente.getSaldo() < fondo.getMontoMinimo()) {
            throw new RuntimeException(
                    "No tiene saldo disponible para vincularse al fondo " + fondo.getNombre()
            );
        }

        // Inicializar lista si es null
        if (cliente.getFondos() == null) {
            cliente.setFondos(new ArrayList<>());
        }

        // Crear fondo cliente
        FondoCliente fondoCliente = FondoCliente.builder()
                .fondoId(fondo.getId())
                .nombre(fondo.getNombre())
                .monto(fondo.getMontoMinimo())
                .fechaApertura(LocalDate.now())
                .build();

        // Restar saldo
        cliente.setSaldo(cliente.getSaldo() - fondo.getMontoMinimo());

        // Agregar fondo
        cliente.getFondos().add(fondoCliente);

        return clienteRepository.save(cliente);
    }

    public Cliente cancelarFondo(String clienteId, Integer fondoId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (cliente.getFondos() == null || cliente.getFondos().isEmpty()) {
            throw new RuntimeException("El cliente no tiene fondos suscritos");
        }

        FondoCliente fondo = cliente.getFondos().stream()
                .filter(f -> f.getFondoId().equals(fondoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Fondo no encontrado en el cliente"));

        // 🔥 DEVOLVER DINERO
        cliente.setSaldo(cliente.getSaldo() + fondo.getMonto());

        // 🔥 ELIMINAR FONDO
        cliente.getFondos().remove(fondo);

        return clienteRepository.save(cliente);
    }
}
