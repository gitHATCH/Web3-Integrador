package org.efa.backend.model.business.interfaces;

import org.efa.backend.model.Cisternado;
import org.efa.backend.model.business.exceptions.BusinessException;

import java.util.List;

public interface ICisternadoBusiness {


    List<Cisternado> list(long idCamion);

    void add(Cisternado cisternado) throws BusinessException;

}
