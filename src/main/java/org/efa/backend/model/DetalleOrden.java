package org.efa.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="detallesOrdenes")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date fechaRecepcionPesajeInicial;

    @Column(nullable = true)
    private Date fechaInicioCarga;

    @Column(nullable = true)
    private Date fechaFinCarga;

    @Column(nullable = true)
    private Date fechaRecepcionFinal;

    @Column(nullable = false)
    private Float pesajeInicial;

    @Column(nullable = true)
    private Float pesajeFinal;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_detalleOrden", nullable = true)
    private List<DetalleCarga> detallesCarga;

    @OneToOne
    @JoinColumn(name = "id_ultimoDetalleCarga",nullable = true)
    private DetalleCarga ultimoDetalleCarga;
}
