package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.DetalleCarga;
import org.efa.backend.model.DetalleOrden;
import org.efa.backend.model.Orden;

import java.util.List;

public interface IOrdenBusiness {
    Orden load(long numero) throws BusinessException, NotFoundException;

    Orden loadById(Long id) throws BusinessException, NotFoundException;

    List<Orden> loadAll() throws BusinessException;

    Orden add(Orden orden) throws FoundException, BusinessException;

    Orden update(Orden orden) throws NotFoundException, BusinessException;

    void delete(long numero) throws NotFoundException, BusinessException;

    void deleteById(long id) throws NotFoundException, BusinessException;

    Orden addTara(Orden orden) throws NotFoundException, BusinessException;

    Orden turnOnBomb(Orden orden) throws NotFoundException, BusinessException;

    DetalleCarga getCargaActual(long numero) throws NotFoundException, BusinessException;

    void cargarCamion(long numero, DetalleCarga detalleCarga) throws BusinessException, NotFoundException;

    Orden turnOffBomb(Long numero, int password) throws NotFoundException, BusinessException;

    Orden cerrarOrden(Long numero) throws BusinessException, NotFoundException;

    Orden concilacion(Long numero) throws BusinessException, NotFoundException;
}
