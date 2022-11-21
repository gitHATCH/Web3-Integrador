package org.efa.backend.integration.cli1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.efa.backend.model.Orden;

import javax.persistence.*;

@Entity
@Table(name="ordenesCli1")
@PrimaryKeyJoinColumn(name = "id_orden")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenCli1 extends Orden {

    @Column(length = 30, nullable = false, unique = true)
    private String codigo;

}
