package com.ceiba.btgpactualms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "transacciones")
public class Transaccion {
    @Id
    private String id;

    private String clienteId;
    private Integer fondoId;
    private TipoTransaccion tipo; // APERTURA o CANCELACION
    private Double monto;
    private LocalDateTime fecha;
}
