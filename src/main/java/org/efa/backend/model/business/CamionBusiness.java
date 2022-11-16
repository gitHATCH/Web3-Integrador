package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Camion;
import org.efa.backend.model.persistence.CamionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CamionBusiness implements ICamionBusiness {

    @Autowired
    private CamionRepository camionDAO;

    @Override
    public Camion load(String codigo) throws BusinessException, NotFoundException {
        Optional<Camion> response;
        try{
            response = camionDAO.findByCodigo(codigo);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el camion con codigo '" + codigo + "'").build();
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

    @Override
    public List<Camion> loadAll() throws BusinessException {
        try {
            return camionDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Camion add(Camion camion) throws FoundException, BusinessException {

            try {
                load(camion.getCodigo());
                throw FoundException.builder().message("Ya existe un camion con codigo '" + camion.getCodigo() + "'").build();
            } catch (NotFoundException ex) {
                try {
                    return camionDAO.save(camion);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().ex(e).build();
                }
            }

    }

    @Override
    public Camion update(Camion camion) throws NotFoundException, BusinessException {
        loadById(camion.getId());
        try {
            return camionDAO.save(camion);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void deleteById(long id) throws NotFoundException, BusinessException {
        loadById(id);
        try {
            camionDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void delete(String codigo) throws NotFoundException, BusinessException {
        Camion camion = load(codigo);
        try {
            camionDAO.deleteById(camion.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

}
