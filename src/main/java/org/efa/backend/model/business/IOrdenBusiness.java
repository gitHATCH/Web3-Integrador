package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Orden;

public interface IOrdenBusiness {
    public Orden load(long numero) throws BusinessException, NotFoundException;

    public Orden loadById(Long id) throws BusinessException, NotFoundException;
}
