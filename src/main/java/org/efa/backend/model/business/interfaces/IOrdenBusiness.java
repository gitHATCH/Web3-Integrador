package org.efa.backend.model.business.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IOrdenBusiness {
    Orden load(long numeroOrden) throws BusinessException, NotFoundException;

    List<Orden> list() throws BusinessException;

    Orden add(Orden orden) throws BusinessException, FoundException, NotFoundException;

    Orden addExternal(String json) throws BusinessException, FoundException, NotFoundException;

    Orden checkIn(String json, long numeroOrden) throws NotFoundException, BusinessException;

    Orden closeOrder(long numeroOrden) throws NotFoundException, BusinessException;

    String checkOut(String json, long numeroOrden) throws NotFoundException, BusinessException, JsonProcessingException;

    String conciliacion(long numeroOrden) throws NotFoundException, JsonProcessingException, BusinessException;

    void aceptarAlarma(long numeroOrden) throws BusinessException;

    void setTemperaturaUmbral(long numeroOrden, float temp) throws  BusinessException;
}
