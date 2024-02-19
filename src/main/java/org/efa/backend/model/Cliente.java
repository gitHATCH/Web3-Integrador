package org.efa.backend.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente implements Serializable {

    private static final long serialVersionUID = 8942462212649854562L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "razon_social")
    private String razonSocial;

    @Column(name = "contacto")
    private long contacto;

    @Column(length = 50, nullable = false, unique = true)
    private String code; //codigo externo
}
