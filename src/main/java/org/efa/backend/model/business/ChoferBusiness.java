package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Chofer;
import org.efa.backend.model.persistence.ChoferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChoferBusiness implements IChoferBusiness{

    @Autowired
    private ChoferRepository choferDAO;

    @Override
    public Chofer load(long dni) throws BusinessException, NotFoundException {
        Optional<Chofer> response;
        try{
            response = choferDAO.findByDni(dni);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el chofer con dni '" + dni + "'").build();
        }
        return response.get();
    }

    @Override
    public Chofer loadById(Long id) throws BusinessException, NotFoundException {
        Optional<Chofer> response;
        try{
            response = choferDAO.findById(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el chofer con id '" + id + "'").build();
        }
        return response.get();
    }

    @Override
    public List<Chofer> loadAll() throws BusinessException {
        try {
            return choferDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Chofer add(Chofer chofer) throws FoundException, BusinessException {
        try {
            load(chofer.getDni());
            throw FoundException.builder().message("Ya existe un chofer con DNI '" + chofer.getDni() +"'").build();
        } catch (NotFoundException ex) {
            try {
                return choferDAO.save(chofer);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().ex(e).build();
            }
        }
    }

    @Override
    public Chofer update(Chofer chofer) throws NotFoundException, BusinessException {
        loadById(chofer.getId());
        try {
            return choferDAO.save(chofer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void deleteById(long id) throws NotFoundException, BusinessException {
        loadById(id);
        try {
            choferDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void delete(long dni) throws NotFoundException, BusinessException {
        Chofer chofer = load(dni);
        try {
            choferDAO.deleteById(chofer.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

}
