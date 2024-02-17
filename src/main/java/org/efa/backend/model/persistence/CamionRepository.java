package org.efa.backend.model.persistence;

import org.efa.backend.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> {

    Optional<Camion> findByCode(String code);
    Boolean existsByCode(String code);
}
