package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Cliente;
import org.efa.backend.model.persistence.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ClienteBusiness implements IClienteBusiness {

    @Autowired
    private ClienteRepository clienteDAO;

    @Override
    public Cliente load(String razonSocial) throws BusinessException, NotFoundException {
        Optional<Cliente> response;
        try{
            response = clienteDAO.findByRazonSocial(razonSocial);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el cliente con razon social '" + razonSocial + "'").build();
        }
        return response.get();
    }

    @Override
    public Cliente loadById(Long id) throws BusinessException, NotFoundException {
        Optional<Cliente> response;
        try{
            response = clienteDAO.findById(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el cliente con id '" + id + "'").build();
        }
        return response.get();
    }

}
