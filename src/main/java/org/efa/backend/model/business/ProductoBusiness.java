package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Producto;
import org.efa.backend.model.persistence.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ProductoBusiness implements IProductoBusiness {

    @Autowired
    private ProductoRepository productoDAO;

    @Override
    public Producto load(String nombre) throws BusinessException, NotFoundException {
        Optional<Producto> response;
        try{
            response = productoDAO.findByNombre(nombre);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el producto con nombre '" + nombre + "'").build();
        }
        return response.get();
    }

    @Override
    public Producto loadById(Long id) throws BusinessException, NotFoundException {
        Optional<Producto> response;
        try{
            response = productoDAO.findById(id);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (response.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra el producto con id '" + id + "'").build();
        }
        return response.get();
    }
}
