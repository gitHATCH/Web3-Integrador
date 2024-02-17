package org.efa.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Producto;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IProductoBusiness;
import org.efa.backend.model.persistence.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoBusiness implements IProductoBusiness {

    @Autowired
    ProductoRepository productoRepository;

    @Override
    public Producto load(String code) throws BusinessException, NotFoundException {
        Optional<Producto> producto;
        try {
            producto = productoRepository.findByCode(code);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (producto.isEmpty()) {
            throw NotFoundException.builder().message("No se encontro el producto con CODIGO: " + code).build();
        }
        return producto.get();
    }

    @Override
    public List<Producto> list() throws BusinessException {
        try {
            return productoRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error al traer lista de Productos.").build();
        }
    }

    @Override
    public Producto add(Producto producto) throws BusinessException, FoundException, NotFoundException {
        try {
            if (productoRepository.existsByCode(producto.getCode())) {
                return load(producto.getCode());
            }
        } catch (NotFoundException e) {
        }
        try {
            return productoRepository.save(producto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error creacion de producto").build();
        }

    }
}
