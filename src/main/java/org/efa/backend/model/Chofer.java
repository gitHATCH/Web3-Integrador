package org.efa.backend.model;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "chofer")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chofer implements Serializable {


    private static final long serialVersionUID = -2600344777206015479L;

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "dni", nullable = false, unique = true)
    private long dni;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(length = 50, nullable = false, unique = true)
    private String code; //codigo externo
}
