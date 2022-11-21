package org.efa.backend.integration.cli1.model.business;

import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.integration.cli1.model.OrdenCli1;

import java.util.List;

public interface IOrdenCli1Business {

    OrdenCli1 load(String codigo) throws NotFoundException, BusinessException;

    List<OrdenCli1> list() throws BusinessException;

    OrdenCli1 add(OrdenCli1 ordenCli1) throws FoundException, BusinessException;


    OrdenCli1 addExternal(String json) throws FoundException, BusinessException;
}
