package org.efa.backend.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private long razonSocial;

    @Column(name = "contacto")
    private long contacto;

    @Column(length = 50, nullable = false, unique = true)
    private String code; //codigo externo
}
