package org.efa.backend.model.business.interfaces;

import org.efa.backend.model.Cliente;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IClienteBusiness {

    Cliente load(String code) throws NotFoundException, BusinessException, FoundException;

    List<Cliente> list() throws BusinessException;

    Cliente add(Cliente cliente) throws NotFoundException, BusinessException, FoundException;
}
