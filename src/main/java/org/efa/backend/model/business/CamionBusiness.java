package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Camion;
import org.efa.backend.model.persistence.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CamionBusiness implements ICamionBusiness {

    @Autowired
    private CamionRepository camionDAO;

    @Override
    public Camion load(String patente) throws BusinessException, NotFoundException {
        Optional<Camion> response;
        try{
            response = camionDAO.findByPatente(patente);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el camion con patente '" + patente + "'").build();
        }
        return response.get();
    }

    @Override
    public Camion loadById(Long id) throws BusinessException, NotFoundException {
        Optional<Camion> response;
        try{
            response = camionDAO.findById(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el camion con id '" + id + "'").build();
        }
        return response.get();
    }
}
