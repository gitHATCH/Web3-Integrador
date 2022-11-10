package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
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

        boolean noForeignFlag = false;
        String[] foreignData = new String[]{};

        try {
            load(orden.getNumero());
            throw FoundException.builder().message("Ya existe la orden numero '" + orden.getNumero()).build();
        } catch (NotFoundException ex) {
            //No existe -> procede a crear
            //Caused by: java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails (`iw3final_db`.`ordenes`, CONSTRAINT `FKs4be0s7apibundgy9mked55xc` FOREIGN KEY (`id_camion`) REFERENCES `camiones` (`id`))
            //TODO: Detectar cuando una foranea no existe y dar mensaje personalizado
            try {

                //Control Camion
                if(orden.getCamion().getId() != null){
                    try {
                        camion.loadById(orden.getCamion().getId());
                    } catch(NotFoundException e){
                        foreignData = new String[]{"camion","id",orden.getCamion().getId().toString()};
                        noForeignFlag = true;
                    }
                }else{
                    if(orden.getCamion().getPatente() != null) {
                        try {
                            orden.setCamion(camion.load(orden.getCamion().getPatente()));
                        } catch (NotFoundException e) {
                            foreignData = new String[]{"camion", "patente", orden.getCamion().getPatente()};
                            noForeignFlag = true;
                        }
                    }
                }

                //Control Chofer
                if(orden.getChofer().getId() != null){
                    try {
                        chofer.loadById(orden.getChofer().getId());
                    } catch(NotFoundException e){
                        foreignData = new String[]{"chofer","id",orden.getChofer().getId().toString()};
                        noForeignFlag = true;
                    }
                }else{
                    if(orden.getChofer().getDni() != null) {
                        try {
                            orden.setChofer(chofer.load(orden.getChofer().getDni()));
                        } catch (NotFoundException e) {
                            foreignData = new String[]{"chofer", "dni", orden.getChofer().getDni().toString()};
                            noForeignFlag = true;
                        }
                    }
                }

                //Control Cliente
                if(orden.getCliente().getId() != null){
                    try {
                        cliente.loadById(orden.getCliente().getId());
                    } catch(NotFoundException e){
                        foreignData = new String[]{"cliente","id",orden.getCliente().getId().toString()};
                        noForeignFlag = true;
                    }
                }else{
                    if(orden.getCliente().getRazonSocial() != null) {
                        try {
                            orden.setCliente(cliente.load(orden.getCliente().getRazonSocial()));
                        } catch (NotFoundException e) {
                            foreignData = new String[]{"cliente", "razon social", orden.getCliente().getRazonSocial()};
                            noForeignFlag = true;
                        }
                    }
                }

                orden.setEstado(1);
                orden.setDetalleOrden(null);
                return ordenDAO.save(orden);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                if(noForeignFlag){
                    throw BusinessException.builder().message("El "+foreignData[0]+" con "+foreignData[1]+" "+foreignData[2]+" no se encuentra").build();
                }else{
                    throw BusinessException.builder().ex(e).build();
                }
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
    public Orden addTara(Orden orden) throws NotFoundException, BusinessException {
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
                        return ordenDAO.save(orden);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw BusinessException.builder().ex(e).build();
                    }
                }
            }
        }else{
            throw BusinessException.builder().message("Es necesario ingresar el detalle de la orden con los datos del pesaje inicial y el producto").build();
        }
    }

    private Orden updateOrden(Orden ordenVieja, Orden ordenNueva) throws BusinessException {
        Random rand = new Random();
        if(ordenNueva.getEstado() == 1) {
            ordenNueva.setDetalleOrden(ordenVieja.getDetalleOrden());
            ordenNueva.setEstado(2);
            ordenNueva.getDetalleOrden().setFechaRecepcionPesajeInicial(new Date());
            ordenNueva.setPassword((int) Math.floor(Math.random()*(MAXIMO-MINIMO+1)+MINIMO));
            return ordenDAO.save(ordenNueva);
        }else{
            throw BusinessException.builder().message("La orden especificada ya pertenece a un estado superior por lo que tiene asignada una tara").build();
        }
    }


}
