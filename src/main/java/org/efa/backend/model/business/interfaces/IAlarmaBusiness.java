package org.efa.backend.model.business.interfaces;

import org.efa.backend.model.Alarma;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;

public interface IAlarmaBusiness {

    Alarma add(long numeroOrden, String nombreUsuario) throws FoundException, BusinessException, NotFoundException;

//    Alarma add(Alarma alarma) throws FoundException, BusinessException, NotFoundException;
}
