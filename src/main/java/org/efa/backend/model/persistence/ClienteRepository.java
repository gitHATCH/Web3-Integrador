package org.efa.backend.model.persistence;

import java.util.Optional;

import org.efa.backend.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository  extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByRazonSocial(String razonSocial);

    Optional<Cliente> findById(Long id);
}
