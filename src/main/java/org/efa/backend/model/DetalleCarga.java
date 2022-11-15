package org.efa.backend.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="detallesCargas")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCarga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Float masa;

    @Column(nullable = false)
    private Float densidad;

    @Column(nullable = false)
    private Float temperatura;

    @Column(nullable = false)
    private Float caudal;

    @Column(nullable = false)
    private Date fechaRecepcionCarga;
}
