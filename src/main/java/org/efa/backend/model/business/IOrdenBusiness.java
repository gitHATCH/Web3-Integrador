package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
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
}
