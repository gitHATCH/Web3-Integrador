package org.efa.backend.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Camion;

import java.util.List;

public interface ICamionBusiness {
    Camion load(String patente) throws BusinessException, NotFoundException;

    Camion loadById(Long id) throws BusinessException, NotFoundException;

    List<Camion> loadAll() throws BusinessException;

    Camion add(Camion camion) throws FoundException, BusinessException;

    Camion update(Camion camion) throws NotFoundException, BusinessException;

    void delete(String patente) throws NotFoundException, BusinessException;

    void deleteById(long id) throws NotFoundException, BusinessException;
}
