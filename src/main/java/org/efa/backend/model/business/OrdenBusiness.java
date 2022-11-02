package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Orden;
import org.efa.backend.model.persistence.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrdenBusiness implements IOrdenBusiness{

    @Autowired
    private OrdenRepository ordenDAO;

    @Override
    public Orden load(long numero) throws BusinessException, NotFoundException {
        Optional<Orden> response;
        try{
            response = ordenDAO.findByNumero(numero);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra la orden numero '" + numero + "'").build();
        }
        return response.get();
    }

    @Override
    public Orden loadById(Long id) throws BusinessException, NotFoundException {
        Optional<Orden> response;
        try{
            response = ordenDAO.findById(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra la orden con id '" + id + "'").build();
        }
        return response.get();
    }
}
