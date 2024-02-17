package org.efa.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Cisternado;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.interfaces.ICisternadoBusiness;
import org.efa.backend.model.persistence.CisternadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CisternadoBusiness implements ICisternadoBusiness {


    @Autowired(required = false)
    private CisternadoRepository cisternadoRepository;

    @Override
    public List<Cisternado> list(long idCamion) {
        return cisternadoRepository.findAllById_IdCamion(idCamion);
    }

    @Override
    public void add(Cisternado cisternado) throws BusinessException {
        try {
            cisternadoRepository.save(cisternado);
        } catch (Exception e) {
            throw BusinessException.builder().ex(e).build();
        }
    }
}
