package org.efa.backend.model.business.interfaces;

import org.efa.backend.model.Producto;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IProductoBusiness {

    Producto load(String code) throws BusinessException, NotFoundException;

    List<Producto> list() throws BusinessException;

    Producto add(Producto producto) throws BusinessException, FoundException, NotFoundException;
}
