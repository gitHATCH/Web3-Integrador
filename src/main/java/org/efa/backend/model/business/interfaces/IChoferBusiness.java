package org.efa.backend.model.business.interfaces;

import org.efa.backend.model.Chofer;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;

import java.util.List;

public interface IChoferBusiness {
    Chofer load(String code) throws NotFoundException, BusinessException;

    List<Chofer> list() throws BusinessException;

    Chofer add(Chofer chofer) throws FoundException, BusinessException, NotFoundException;
}
