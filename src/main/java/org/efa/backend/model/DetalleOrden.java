package org.efa.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="detallesOrdenes")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrden {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private Date fechaRecepcionCarga;

    @Column(nullable = true)
    private Date fechaRecepcionPesajeInicial;

    @Column(nullable = true)
    private Date fechaInicioCarga;

    @Column(nullable = true)
    private Date fechaFinCarga;

    @Column(nullable = true)
    private Date fechaRecepcionFinal;

    @Column(nullable = false)
    private Integer pesajeInicial;

    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="id_producto", nullable = false)
    private Producto producto;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "id_detalleCarga", nullable = true)
    private DetalleCarga detalleCarga;
}