package org.efa.backend.model.persistence;

import org.efa.backend.model.Alarma;
import org.efa.backend.model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmaRepository extends JpaRepository<Alarma, String> {

}
