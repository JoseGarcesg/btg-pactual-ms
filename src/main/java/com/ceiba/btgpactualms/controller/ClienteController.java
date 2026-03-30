package com.ceiba.btgpactualms.controller;

import com.ceiba.btgpactualms.dto.SuscripcionRequest;
import com.ceiba.btgpactualms.model.Cliente;
import com.ceiba.btgpactualms.repository.ClienteRepository;
import com.ceiba.btgpactualms.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository repository;
    private final ClienteService clienteService;

    @PostMapping
    public Cliente createClient(@RequestBody Cliente cliente) {
        cliente.setSaldo(500000.0);
        return repository.save(cliente);
    }

    @GetMapping
    public List<Cliente> listClients() {
        return repository.findAll();
    }

    @PostMapping("/{id}/fondos")
    public Cliente suscribeClient(
            @PathVariable String id,
            @RequestBody SuscripcionRequest request
    ) {
        return clienteService.suscribirse(id, request);
    }

    @DeleteMapping("/{id}/fondos/{fondoId}")
    public Cliente cancelar(
            @PathVariable String id,
            @PathVariable Integer fondoId
    ) {
        return clienteService.cancelarFondo(id, fondoId);
    }
}
