package org.efa.backend.auth;

import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.NotFoundException;

public interface IRoleBusiness {
    Role add(Role role) throws NotFoundException, BusinessException;

    Role load(String name) throws NotFoundException, BusinessException;
}
