package org.efa.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="ordenes")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoExterno;

    @Column(nullable = false, unique = true)
    private Long numero;

    @Column(nullable = false)
    private Integer estado;

    @Column(nullable = false)
    private Float preset;

    @Column(length = 5, nullable = true)
    private Integer password;

    @Column(nullable = false)
    private Date fechaCargaPrevista;

    @ManyToOne
    @JoinColumn(name="id_camion", nullable = false)
    private Camion camion;

    @ManyToOne
    @JoinColumn(name="id_chofer", nullable = false)
    private Chofer chofer;

    @ManyToOne
    @JoinColumn(name="id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name="id_producto", nullable = false)
    private Producto producto;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_detalleOrden", nullable = true)
    private DetalleOrden detalleOrden;

}
