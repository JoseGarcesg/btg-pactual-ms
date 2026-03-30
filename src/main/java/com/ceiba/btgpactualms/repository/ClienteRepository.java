package com.ceiba.btgpactualms.repository;

import com.ceiba.btgpactualms.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente,String> {
}
