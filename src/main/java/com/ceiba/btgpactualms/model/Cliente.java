package com.ceiba.btgpactualms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "clientes")
public class Cliente {

    @Id
    private String id;

    private String nombre;
    private String email;
    private String telefono;
    private CanalNotificacion preferenciaNotificacion;

    private Double saldo;

    private List<FondoCliente> fondos;
}
