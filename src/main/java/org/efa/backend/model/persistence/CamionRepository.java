package org.efa.backend.model.persistence;

import java.util.Optional;
import org.efa.backend.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {
    Optional<Camion> findByCodigo(String codigo);

    Optional<Camion> findById(Long id);

    void deleteById(Long id);
}
