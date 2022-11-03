package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Producto;

import java.util.List;

public interface IProductoBusiness {
    Producto load(String nombre) throws BusinessException, NotFoundException;

    Producto loadById(Long id) throws BusinessException, NotFoundException;

    List<Producto> loadAll() throws BusinessException;

    Producto add(Producto producto) throws FoundException, BusinessException;

    Producto update(Producto producto) throws NotFoundException, BusinessException;

    void delete(String nombre) throws NotFoundException, BusinessException;

    void deleteById(long id) throws NotFoundException, BusinessException;
}
