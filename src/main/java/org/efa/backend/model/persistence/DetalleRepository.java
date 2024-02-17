package org.efa.backend.model.persistence;
import org.efa.backend.model.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleRepository extends JpaRepository<Detalle, Long> {

    @Query(value = "select * from detalle where numero_orden = ?", nativeQuery = true)
    List<Detalle> findAllById_NumeroOrden(long numeroOrden);

    Boolean existsDetalleByOrden_numeroOrden(long numeroOrden);
}

