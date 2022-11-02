package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Producto;

public interface IProductoBusiness {
    public Producto load(String nombre) throws BusinessException, NotFoundException;

    public Producto loadById(Long id) throws BusinessException, NotFoundException;
}
