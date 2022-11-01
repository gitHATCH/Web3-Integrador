package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Chofer;
import org.efa.backend.model.persistence.ChoferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
