package org.efa.backend.model.persistence;

import org.efa.backend.model.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, String> {

    Optional<Chofer> findByCode(String code);
    Boolean existsByCode(String code);

    Boolean existsByDni(long dni);

}
