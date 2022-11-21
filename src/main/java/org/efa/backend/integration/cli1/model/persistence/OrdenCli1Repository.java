package org.efa.backend.integration.cli1.model.persistence;

import org.efa.backend.integration.cli1.model.OrdenCli1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrdenCli1Repository extends JpaRepository<OrdenCli1, Long>{
    Optional<OrdenCli1> findByCodigo(String codigo);

    @Query("select id")
    Long getNumero(String codigo);
}
