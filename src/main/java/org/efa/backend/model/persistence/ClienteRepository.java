package org.efa.backend.model.persistence;

import org.efa.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByCode(String code);
    Boolean existsByCode(String code);

    Boolean existsByRazonSocial(String razonSocial);
}
