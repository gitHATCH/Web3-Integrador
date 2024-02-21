package org.efa.backend.model;


import io.swagger.v3.oas.annotations.Hidden;
import lombok.*;
import org.efa.backend.auth.User;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "alarma")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Alarma {

    private static final long serialVersionUID = -2162240618874701205L;

    @Id
    @Hidden
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fecha")
    private OffsetDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable=false)
    private User usuario;

    @Column(name = "orden", nullable = false)
    private long numeroOrden;
}
