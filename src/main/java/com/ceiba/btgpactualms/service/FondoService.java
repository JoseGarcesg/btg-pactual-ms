package com.ceiba.btgpactualms.service;

import com.ceiba.btgpactualms.model.Fondo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FondoService {
    private final List<Fondo> fondos = List.of(
            new Fondo(1, "FPV_BTG_PACTUAL_RECAUDADORA", 75000.0),
            new Fondo(2, "FPV_BTG_PACTUAL_ECOPETROL", 125000.0),
            new Fondo(3, "DEUDAPRIVADA", 50000.0),
            new Fondo(4, "FDO-ACCIONES", 250000.0),
            new Fondo(5, "FPV_BTG_PACTUAL_DINAMICA", 100000.0)
    );

    public Optional<Fondo> getById(Integer id) {
        return fondos.stream().filter(f -> f.getId().equals(id)).findFirst();
    }
}
