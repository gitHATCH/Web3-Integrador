package org.efa.backend.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "orden")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orden implements Serializable {

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "numero_orden", nullable = false, unique = true)
    private long numeroOrden;

    @Column(name = "codigo_externo",nullable = false, unique = true)
    private String CodigoExterno;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_camion")
    private Camion camion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_chofer")
    private Chofer chofer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto")
    private Producto producto;

    @Hidden
    @OneToMany(mappedBy = "orden", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Detalle> detalle;

    //VER FECHAS!!!!
    @Column(name = "preset")
    private Float preset;

    @Column(name = "fecha_turno_carga")
    private OffsetDateTime fechaTurnoCarga;

    @Hidden
    @Column(name = "fecha_pesaje_inicial")
    private OffsetDateTime fechaPesajeInicial;

    @Hidden
    @Column(name = "estado")
    private int estado;

    @Hidden
    @Column(name = "tara")
    private long tara;

    @Hidden
    @Column(name = "password")
    private long password;

    @Hidden
    @Column(name = "alarma")
    private boolean alarma; //indica si la alarma de la temperatura para una orden fue aceptada o no.
    @Hidden
    @Column(name = "temperatura_umbral")
    private float temperaturaUmbral;

    //Ultimos valores medidos del detalle de carga
    @Hidden
    @Column(name = "ultima_masa")
    private float ultimaMasa;
    @Hidden
    @Column(name = "ultima_densidad")
    private float ultimaDensidad;
    @Hidden
    @Column(name = "ultima_temperatura")
    private float ultimaTemperatura;
    @Hidden
    @Column(name = "ultimo_caudal")
    private float ultimoCaudal;

    @Hidden
    @Column(name = "pesaje_final")
    private long pesajeFinal;

    @Hidden
    @Column(name = "fecha_pesaje_final")
    private OffsetDateTime fechaPesajeFinal;

    @Hidden
    @Column(name = "fecha_detalle_final")
    private OffsetDateTime fechaDetalleFinal;

    @Hidden
    @Column(name = "fecha_detalle_inicial")
    private OffsetDateTime fechaDetalleInicial;
}
