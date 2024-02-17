package org.efa.backend.model.persistence;

import org.efa.backend.model.Cisternado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CisternadoRepository extends JpaRepository<Cisternado, Long> {

    //TODO: hacer la consulta manual!!
    @Query(value = "select * from cisternado where id_camion = ?", nativeQuery = true)
    List<Cisternado> findAllById_IdCamion(long idCamion);
}
