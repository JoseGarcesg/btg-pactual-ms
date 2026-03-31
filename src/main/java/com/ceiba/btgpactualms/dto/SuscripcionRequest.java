package com.ceiba.btgpactualms.dto;

import lombok.Data;

@Data
public class SuscripcionRequest {
    private String clienteId;
    private Integer fondoId;
    private Double monto;
}
