package com.ceiba.btgpactualms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;

    private String username;
    private String password;
    private String rol;
}
