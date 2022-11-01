package org.efa.backend.model.persistence;

import java.util.Optional;
import org.efa.backend.model.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long>{
    Optional<Chofer> findByDni(long dni);
}