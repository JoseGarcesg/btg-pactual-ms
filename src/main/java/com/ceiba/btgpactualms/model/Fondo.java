package com.ceiba.btgpactualms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fondo {
    private Integer id;
    private String nombre;
    private Double montoMinimo;

}
