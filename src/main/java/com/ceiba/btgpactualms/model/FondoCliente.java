package com.ceiba.btgpactualms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FondoCliente {

    private Integer fondoId;
    private String nombre;
    private Double monto;
    private LocalDate fechaApertura;
}
