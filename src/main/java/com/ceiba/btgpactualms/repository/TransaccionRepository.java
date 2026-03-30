package com.ceiba.btgpactualms.repository;

import com.ceiba.btgpactualms.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByClienteId(String clienteId);
}
