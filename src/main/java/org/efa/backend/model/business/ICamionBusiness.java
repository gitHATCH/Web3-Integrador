package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Camion;

public interface ICamionBusiness {
    public Camion load(String patente) throws BusinessException, NotFoundException;

    public Camion loadById(Long id) throws BusinessException, NotFoundException;
}
