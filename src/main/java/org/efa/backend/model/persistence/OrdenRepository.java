package org.efa.backend.model.persistence;

import org.efa.backend.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    Optional<Orden> findByNumero(long numero);

    Optional<Orden> findById(Long id);

    void deleteById(Long id);

    @Query(value = "SELECT id FROM ordenes o where o.password = :passValue AND o.preset = :presetValue", nativeQuery = true)
    Long idOrdenBomb(Integer passValue, Integer presetValue);
}
