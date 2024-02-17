package org.efa.backend.model.persistence;

import org.efa.backend.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    Optional<Producto> findByCode(String code);

    Boolean existsByCode(String code);
}
