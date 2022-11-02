package org.efa.backend.model.persistence;

import org.efa.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByNumero(long numero);

    Optional<Orden> findById(Long id);
}
