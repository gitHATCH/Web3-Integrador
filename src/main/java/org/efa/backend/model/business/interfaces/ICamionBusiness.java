package org.efa.backend.model.business.interfaces;

import org.efa.backend.model.Camion;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface ICamionBusiness {

    Camion load(String code) throws NotFoundException, BusinessException, FoundException;

    Boolean exists(String code) throws NotFoundException, BusinessException, FoundException;

    List<Camion> list() throws BusinessException;

    Camion add(Camion camion) throws FoundException, BusinessException, NotFoundException;

}
