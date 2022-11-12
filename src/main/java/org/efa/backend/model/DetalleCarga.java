package org.efa.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="detallesCargas")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCarga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private float masa;

    @Column(nullable = false)
    private float densidad;

    @Column(nullable = false)
    private float temperatura;

    @Column(nullable = false)
    private float caudal;

    @Column(nullable = false)
    private Date fechaRecepcionCarga;
}
