package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Cliente;
import org.efa.backend.model.persistence.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public List<Cliente> loadAll() throws BusinessException {
        try {
            return clienteDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Cliente add(Cliente cliente) throws FoundException, BusinessException {
        try {
            load(cliente.getRazonSocial());
            throw FoundException.builder().message("Ya existe un cliente con razon social '" + cliente.getRazonSocial() +"'").build();
        } catch (NotFoundException ex) {
            //No existe -> procede a crear
            try {
                return clienteDAO.save(cliente);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().ex(e).build();
            }
        }
    }

    @Override
    public Cliente update(Cliente cliente) throws NotFoundException, BusinessException {
        loadById(cliente.getId());
        try {
            return clienteDAO.save(cliente);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void deleteById(long id) throws NotFoundException, BusinessException {
        loadById(id);
        try {
            clienteDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void delete(String razonSocial) throws NotFoundException, BusinessException {
        Cliente cliente = load(razonSocial);
        try {
            clienteDAO.deleteById(cliente.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

}
