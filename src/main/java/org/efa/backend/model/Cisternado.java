package org.efa.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "cisternado")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cisternado implements Serializable {


    private static final long serialVersionUID = -1804488154297204890L;

    @Hidden
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_cisterna;

    @ManyToOne
    @JoinColumn(name = "patente")
    @JsonIgnore
    private Camion camion;

    @Column
    private long tamanio;
}
