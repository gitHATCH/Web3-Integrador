package org.efa.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Chofer;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IChoferBusiness;
import org.efa.backend.model.persistence.ChoferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChoferBusiness implements IChoferBusiness {

    @Autowired
    private ChoferRepository choferRepository;

    @Override
    public Chofer load(String code) throws NotFoundException, BusinessException {
        Optional<Chofer> chofer;
        try {
            chofer = choferRepository.findByCode(code);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        if (chofer.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro el chofer con CODIGO: " + code).build();
        }
        return chofer.get();
    }

    @Override
    public List<Chofer> list() throws BusinessException {
        try {
            return choferRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error al traer todos los Choferes.").build();
        }

    }

    @Override
    public Chofer add(Chofer chofer) throws FoundException, BusinessException, NotFoundException {
        if (choferRepository.existsByCode(chofer.getCode())) {
//                return load(chofer.getCode());
            throw FoundException.builder().message("Ya existe un chofer con el CODIGO: " + chofer.getCode()).build();
        }
        try {
            return choferRepository.save(chofer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error de creacion de chofer").build();
        }
    }
}
//El update no iria, pq no podriamos modificar ninguno.