package org.efa.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "producto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Producto implements Serializable {

    private static final long serialVersionUID = -2510094698283791123L;

    @Id
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(length = 50, nullable = false, unique = true)
    private String code; //codigo externo
}
