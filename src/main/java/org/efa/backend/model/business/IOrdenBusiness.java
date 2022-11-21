package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.DetalleCarga;
import org.efa.backend.model.Orden;
import org.efa.backend.model.views.IConciliacionSlimView;

import java.util.List;

public interface IOrdenBusiness {

    Orden loadById(Long id) throws BusinessException, NotFoundException;

    Orden load(long numero) throws BusinessException, NotFoundException;

    List<Orden> loadAll() throws BusinessException;

    Orden add(Orden orden) throws FoundException, BusinessException;

    Orden update(Orden orden) throws NotFoundException, BusinessException;

    void delete(long numero) throws NotFoundException, BusinessException;

    void deleteById(long id) throws NotFoundException, BusinessException;

    Orden addTara(Long numero, Float tara) throws NotFoundException, BusinessException;

    Orden turnOnBomb(Long numero) throws NotFoundException, BusinessException;

    DetalleCarga getCargaActual(long numero) throws NotFoundException, BusinessException;

    void cargarCamion(long numero, DetalleCarga detalleCarga) throws BusinessException, NotFoundException;

    Orden turnOffBomb(Long numero) throws NotFoundException, BusinessException;

    IConciliacionSlimView cerrarOrden(Long numero) throws BusinessException, NotFoundException;

    IConciliacionSlimView concilacion(Long numero) throws BusinessException, NotFoundException;

//    public Orden addExternal(String json) throws FoundException, BusinessException;
}
