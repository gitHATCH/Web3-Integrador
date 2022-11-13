package org.efa.backend.model.views;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conciliacion {
    Long numeroOrden;
    float pesajeIncial;
    float pesajeFinal;
    float masa; //producto cargado (ultimo valor de masa acumulada)
    //Neto x balanza (getPesajeFinal - getPesajeIncial)
    //Diferencia balanza-caudalimetro (Neto x balanza - getMasa)
    float promedioTemperatura;
    float promedioDensidad;
    float promedioCaudal;



}
