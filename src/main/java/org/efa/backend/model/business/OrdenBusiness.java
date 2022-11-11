package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.aspectj.weaver.ast.Or;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.DetalleOrden;
import org.efa.backend.model.Orden;
import org.efa.backend.model.persistence.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OrdenBusiness implements IOrdenBusiness{

    @Autowired
    private OrdenRepository ordenDAO;

    @Autowired
    private CamionBusiness camion;

    @Autowired
    private ChoferBusiness chofer;

    @Autowired
    private ProductoBusiness producto;

    @Autowired
    private ClienteBusiness cliente;
    private final int MINIMO = 99999;
    private final int MAXIMO = 10000;

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

    @Override
    public List<Orden> loadAll() throws BusinessException {
        try {
            return ordenDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Orden add(Orden orden) throws FoundException, BusinessException{
        try {
            load(orden.getNumero());
            throw FoundException.builder().message("Ya existe la orden numero '" + orden.getNumero()).build();
        } catch (NotFoundException ex) {
            try {
                //Control Entidades
                addOrdenCamionController(orden);
                addOrdenChoferController(orden);
                addOrdenClienteController(orden);
                addOrdenProductoController(orden);

                orden.setEstado(1);
                orden.setDetalleOrden(null);
                return ordenDAO.save(orden);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().ex(e).build();
            }
        }
    }

    @Override
    public Orden update(Orden orden) throws NotFoundException, BusinessException {
        loadById(orden.getId());
        try {
            return ordenDAO.save(orden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void deleteById(long id) throws NotFoundException, BusinessException {
        loadById(id);
        try {
            ordenDAO.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void delete(long numero) throws NotFoundException, BusinessException {
        Orden orden = load(numero);
        try {
            ordenDAO.deleteById(orden.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Integer addTara(Orden orden) throws NotFoundException, BusinessException {
        Orden ordenNueva;
        if(orden.getDetalleOrden() != null) {
            if (orden.getId() != null) {
                try {
                    ordenNueva = loadById(orden.getId());
                    return updateOrden(orden, ordenNueva);
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw NotFoundException.builder().message("No se encuentra la orden con id " + orden.getId()).build();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().ex(e).build();
                }
            } else {
                if (orden.getNumero() != null) {
                    try {
                        ordenNueva = load(orden.getNumero());
                        return updateOrden(orden, ordenNueva);
                    } catch (NotFoundException e) {
                        log.error(e.getMessage(), e);
                        throw NotFoundException.builder().message("No se encuentra la orden con numero " + orden.getNumero()).build();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw BusinessException.builder().ex(e).build();
                    }
                } else {
                    try {
                        return ordenDAO.save(orden).getPassword();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw BusinessException.builder().ex(e).build();
                    }
                }
            }
        }else{
            throw BusinessException.builder().message("Es necesario ingresar el detalle de la orden con los datos del pesaje inicial").build();
        }
    }

    private Integer updateOrden(Orden ordenVieja, Orden ordenNueva) throws BusinessException {
        Random rand = new Random();
        if(ordenNueva.getEstado() == 1) {
            ordenNueva.setDetalleOrden(ordenVieja.getDetalleOrden());
            ordenNueva.setEstado(2);
            ordenNueva.getDetalleOrden().setFechaRecepcionPesajeInicial(new Date());
            ordenNueva.setPassword((int) Math.floor(Math.random()*(MAXIMO-MINIMO+1)+MINIMO));
            return ordenDAO.save(ordenNueva).getPassword();
        }else{
            throw BusinessException.builder().message("La orden especificada ya pertenece a un estado superior por lo que tiene asignada una tara").build();
        }
    }


    private void addOrdenCamionController(Orden orden) throws BusinessException {
                if(orden.getCamion().getId() != null){
                    try {
                        camion.loadById(orden.getCamion().getId());
                    } catch(NotFoundException e){
                        log.error(e.getMessage(), e);
                        throw BusinessException.builder().message("El camion de id "+orden.getCamion().getId()+" no se encuentra").build();
                    }
                }else{
                    if(orden.getCamion().getPatente() != null) {
                        try {
                            orden.setCamion(camion.load(orden.getCamion().getPatente()));
                        } catch (NotFoundException e) {
                            log.error(e.getMessage(), e);
                            throw BusinessException.builder().message("El camion de patente "+orden.getCamion().getPatente()+" no se encuentra").build();
                        }
                    }
                }
    }

    private void addOrdenChoferController(Orden orden) throws BusinessException {
        if(orden.getChofer().getId() != null){
            try {
                chofer.loadById(orden.getChofer().getId());
            } catch(NotFoundException e){
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El chofer de id "+orden.getChofer().getId()+" no se encuentra").build();
            }
        }else{
            if(orden.getChofer().getDni() != null) {
                try {
                    orden.setChofer(chofer.load(orden.getChofer().getDni()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El chofer de dni "+orden.getChofer().getDni()+" no se encuentra").build();
                }
            }
        }
    }

    private void addOrdenClienteController(Orden orden) throws BusinessException {
        if(orden.getCliente().getId() != null){
            try {
                cliente.loadById(orden.getCliente().getId());
            } catch(NotFoundException e){
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El cliente de id "+orden.getCliente().getId()+" no se encuentra").build();
            }
        }else{
            if(orden.getCliente().getRazonSocial() != null) {
                try {
                    orden.setCliente(cliente.load(orden.getCliente().getRazonSocial()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El cliente de razon social "+orden.getCliente().getRazonSocial()+" no se encuentra").build();
                }
            }
        }
    }

    private void addOrdenProductoController(Orden orden) throws BusinessException {
        if(orden.getProducto().getId() != null){
            try {
                producto.loadById(orden.getProducto().getId());
            } catch(NotFoundException e){
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El producto de id "+orden.getProducto().getId()+" no se encuentra").build();
            }
        }else{
            if(orden.getProducto().getNombre() != null) {
                try {
                    orden.setProducto(producto.load(orden.getProducto().getNombre()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El producto de nombre "+orden.getProducto().getNombre()+" no se encuentra").build();
                }
            }
        }
    }




}
