package org.efa.backend.model.views;

public interface IConciliacionSlimView {
    Long getNumero();
    Float getPesajeInicial();
    Float getPesajeFinal();
    Float getNetoBalanza();
    Float getPromedioDensidad();
    Float getPromedioTemperatura();
    Float getPromedioCaudal();
    Float getProductoCargado();
    Float getDiferencia();

}