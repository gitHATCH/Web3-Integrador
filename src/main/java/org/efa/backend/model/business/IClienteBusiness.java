package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Cliente;

import java.util.List;

public interface IClienteBusiness {
    Cliente load(String razonSocial) throws BusinessException, NotFoundException;

    Cliente loadById(Long id) throws BusinessException, NotFoundException;

    List<Cliente> loadAll() throws BusinessException;

    Cliente add(Cliente cliente) throws FoundException, BusinessException;

    Cliente update(Cliente cliente) throws NotFoundException, BusinessException;

    void delete(String razonSocial) throws NotFoundException, BusinessException;

    void deleteById(long id) throws NotFoundException, BusinessException;
}
