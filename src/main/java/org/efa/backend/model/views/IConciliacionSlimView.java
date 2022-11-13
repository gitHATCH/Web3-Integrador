package org.efa.backend.model.views;

public interface IConciliacionSlimView {
    float getPesajeIncial();
    float getPesajeFinal();
    float getMasa(); //producto cargado (ultimo valor de masa acumulada)
    //Neto x balanza (getPesajeFinal - getPesajeIncial)
    //Diferencia balanza-caudalimetro (Neto x balanza - getMasa)
    float getPromedioTemperatura();
    float getPromedioDensidad();
    float getPromedioCaudal();
}
