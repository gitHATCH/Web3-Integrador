package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Cliente;

public interface IClienteBusiness {
    public Cliente load(String razonSocial) throws BusinessException, NotFoundException;

    public Cliente loadById(Long id) throws BusinessException, NotFoundException;
}
