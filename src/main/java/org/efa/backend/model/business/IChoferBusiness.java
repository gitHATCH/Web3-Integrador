package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Chofer;

import java.util.List;

public interface IChoferBusiness {
    Chofer load(String codigo) throws BusinessException, NotFoundException;

    Chofer loadById(Long id) throws BusinessException, NotFoundException;

    List<Chofer> loadAll() throws BusinessException;

    Chofer add(Chofer chofer) throws FoundException, BusinessException;

    Chofer update(Chofer chofer) throws NotFoundException, BusinessException;

    void delete(String codigo) throws NotFoundException, BusinessException;

    void deleteById(long id) throws NotFoundException, BusinessException;
}
