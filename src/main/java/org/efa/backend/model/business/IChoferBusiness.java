package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Chofer;

public interface IChoferBusiness {
    public Chofer load(long dni) throws BusinessException, NotFoundException;
}
