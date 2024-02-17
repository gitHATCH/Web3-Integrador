package org.efa.backend.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "camion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Camion implements Serializable {

    private static final long serialVersionUID = -2162240618874701205L;

    @Id
    @Column(length = 10, unique = true)
    private String patente;

    @Column
    private String descripcion;


    @OneToMany(mappedBy = "camion", fetch = FetchType.EAGER)
    List<Cisternado> datosCisterna;

    @Column
    private long totalCisterna;

    @Column(length = 50, nullable = false, unique = true)
    private String code; //codigo externo

}
