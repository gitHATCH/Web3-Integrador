package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.DetalleCarga;
import org.efa.backend.model.Orden;
import org.efa.backend.model.persistence.OrdenRepository;
import org.efa.backend.utils.EmailBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class OrdenBusiness implements IOrdenBusiness {

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

    @Autowired
    private EmailBusiness emailBusiness;

    @Override
    public Orden load(long numero) throws BusinessException, NotFoundException {
        Optional<Orden> response;
        try {
            response = ordenDAO.findByNumero(numero);
        } catch (Exception e) {
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
        try {
            response = ordenDAO.findById(id);
        } catch (Exception e) {
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
    public Orden add(Orden orden) throws FoundException, BusinessException {
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
    public Orden addTara(Orden orden) throws NotFoundException, BusinessException {
        Orden ordenNueva;
        if (orden.getDetalleOrden() != null) {
            if (orden.getId() != null) {
                try {
                    ordenNueva = loadById(orden.getId());
                    return upgradeOrden(orden, ordenNueva);
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
                        return upgradeOrden(orden, ordenNueva);
                    } catch (NotFoundException e) {
                        log.error(e.getMessage(), e);
                        throw NotFoundException.builder().message("No se encuentra la orden con numero " + orden.getNumero()).build();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw BusinessException.builder().ex(e).build();
                    }
                } else {
                    try {
                        return ordenDAO.save(orden); //Dudas respecto a esto
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        throw BusinessException.builder().ex(e).build();
                    }
                }
            }
        } else {
            throw BusinessException.builder().message("Es necesario ingresar el detalle de la orden con los datos del pesaje inicial").build();
        }
    }

//    @Override
//    public Orden turnOnBomb(Orden orden) throws NotFoundException, BusinessException {
//        if(orden.getNumero() != null && orden.getPassword() != null){
//            try {
//                Orden ordenNueva = load(orden.getNumero());
//                if(ordenNueva.getPassword().equals(orden.getPassword())) {
//                    if (ordenNueva.getEstado() == 2 && ordenNueva.getDetalleOrden().getDetallesCarga().isEmpty()) {
//                        ordenNueva.getDetalleOrden().setFechaInicioCarga(new Date());
//                        ordenNueva.getDetalleOrden().getDetallesCarga().add(new DetalleCarga());
//                        ordenNueva.getDetalleOrden().getDetallesCarga().get(0).setFechaRecepcionCarga(new Date());
//                        ordenNueva.getDetalleOrden().getDetallesCarga().get(0).setMasa(0);
//                        ordenNueva.getDetalleOrden().getDetallesCarga().get(0).setDensidad(0);
//                        ordenNueva.getDetalleOrden().getDetallesCarga().get(0).setCaudal(0);
//                        ordenNueva.getDetalleOrden().getDetallesCarga().get(0).setTemperatura(0);
//                        //Encender Schedule de carga
//                        return ordenDAO.save(ordenNueva);
//                    } else {
//                        throw BusinessException.builder().message("La orden asociada ya se encuentra en proceso").build();
//                    }
//                }else{
//                    throw NotFoundException.builder().message("No se encontró ninguna orden asociada a esa password").build();
//                }
//            }catch (NotFoundException e){
//                log.error(e.getMessage(), e);
//                throw NotFoundException.builder().ex(e).build();
//            }
//            catch (Exception e){
//                log.error(e.getMessage(), e);
//                throw BusinessException.builder().ex(e).build();
//            }
//        }else{
//            throw BusinessException.builder().message("Es encesario ingresar el numero de orden y la password asociada").build();
//        }
//    }


    private Orden upgradeOrden(Orden ordenVieja, Orden ordenNueva) throws BusinessException {
        final int MINIMO = 99999;
        final int MAXIMO = 10000;
        if (ordenNueva.getEstado() == 1) {
            ordenNueva.setDetalleOrden(ordenVieja.getDetalleOrden());
            ordenNueva.setEstado(2);
            ordenNueva.getDetalleOrden().setDetallesCarga(null);
            ordenNueva.getDetalleOrden().setFechaRecepcionPesajeInicial(new Date());
            ordenNueva.setPassword((int) Math.floor(Math.random() * (MAXIMO - MINIMO + 1) + MINIMO));
            if(System.getenv("MAIL_USERNAME") != null){
                emailBusiness.sendSimpleMessage(System.getenv("MAIL_USERNAME"),"Clave generada exitosamente","Su ping generado es: '"+ordenNueva.getPassword()+"' . Por favor conservelo por seguridad");
            }
            return ordenDAO.save(ordenNueva);
        } else {
            throw BusinessException.builder().message("La orden especificada ya pertenece a un estado superior por lo que tiene asignada una tara").build();
        }
    }


    private void addOrdenCamionController(Orden orden) throws BusinessException {
        if (orden.getCamion().getId() != null) {
            try {
                camion.loadById(orden.getCamion().getId());
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El camion de id " + orden.getCamion().getId() + " no se encuentra").build();
            }
        } else {
            if (orden.getCamion().getPatente() != null) {
                try {
                    orden.setCamion(camion.load(orden.getCamion().getPatente()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El camion de patente " + orden.getCamion().getPatente() + " no se encuentra").build();
                }
            }
        }
    }

    private void addOrdenChoferController(Orden orden) throws BusinessException {
        if (orden.getChofer().getId() != null) {
            try {
                chofer.loadById(orden.getChofer().getId());
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El chofer de id " + orden.getChofer().getId() + " no se encuentra").build();
            }
        } else {
            if (orden.getChofer().getDni() != null) {
                try {
                    orden.setChofer(chofer.load(orden.getChofer().getDni()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El chofer de dni " + orden.getChofer().getDni() + " no se encuentra").build();
                }
            }
        }
    }

    private void addOrdenClienteController(Orden orden) throws BusinessException {
        if (orden.getCliente().getId() != null) {
            try {
                cliente.loadById(orden.getCliente().getId());
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El cliente de id " + orden.getCliente().getId() + " no se encuentra").build();
            }
        } else {
            if (orden.getCliente().getRazonSocial() != null) {
                try {
                    orden.setCliente(cliente.load(orden.getCliente().getRazonSocial()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El cliente de razon social " + orden.getCliente().getRazonSocial() + " no se encuentra").build();
                }
            }
        }
    }

    private void addOrdenProductoController(Orden orden) throws BusinessException {
        if (orden.getProducto().getId() != null) {
            try {
                producto.loadById(orden.getProducto().getId());
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().message("El producto de id " + orden.getProducto().getId() + " no se encuentra").build();
            }
        } else {
            if (orden.getProducto().getNombre() != null) {
                try {
                    orden.setProducto(producto.load(orden.getProducto().getNombre()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El producto de nombre " + orden.getProducto().getNombre() + " no se encuentra").build();
                }
            }
        }
    }

    @Override
    public Orden turnOnBomb(Orden orden) throws NotFoundException, BusinessException {
        if (orden.getNumero() != null && orden.getPassword() != null) {
            try {

                Orden ordenActiva = load(orden.getNumero());
                if (!ordenActiva.getPassword().equals(orden.getPassword())) {
                    throw NotFoundException.builder().message("La password y el numero de orden no coinciden").build();
                }
                if (ordenActiva.getEstado() != 2 || !ordenActiva.getDetalleOrden().getDetallesCarga().isEmpty()) {
                    throw BusinessException.builder().message("La orden asociada ya se encuentra en proceso").build();
                }
                ordenActiva.getDetalleOrden().setFechaInicioCarga(new Date());
                ordenActiva.getDetalleOrden().getDetallesCarga().add(new DetalleCarga());
                ordenActiva.getDetalleOrden().getDetallesCarga().get(0).setFechaRecepcionCarga(new Date());
                ordenActiva.getDetalleOrden().getDetallesCarga().get(0).setMasa(0);
                ordenActiva.getDetalleOrden().getDetallesCarga().get(0).setDensidad(0);
                ordenActiva.getDetalleOrden().getDetallesCarga().get(0).setCaudal(0);
                ordenActiva.getDetalleOrden().getDetallesCarga().get(0).setTemperatura(0);
                //Encender Schedule de carga
                return ordenDAO.save(ordenActiva);

            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
                throw NotFoundException.builder().ex(e).build();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().ex(e).build();
            }
        } else {
            throw BusinessException.builder().message("Es encesario ingresar el numero de orden y la password asociada").build();
        }
    }

    @Override
    public DetalleCarga getCargaActual(long numero) throws BusinessException, NotFoundException {
        try {
            Orden orden = load(numero);
            if (orden.getEstado() != 2 || orden.getDetalleOrden().getDetallesCarga().isEmpty()) {
                throw BusinessException.builder().message("La orden correspondiente no se encuentra en proceso de carga").build();
            } else {
                return orden.getDetalleOrden().getDetallesCarga().get(orden.getDetalleOrden().getDetallesCarga().size() - 1);
            }
        } catch (NotFoundException e) {
            log.error(e.getMessage(), e);
            throw NotFoundException.builder().message("La orden numero " + numero + " no se encuentra").build();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void cargarCamion(long numero, DetalleCarga detalleCarga) throws BusinessException, NotFoundException {
        Orden orden = load(numero);

        //VALIDA QUE LA BOMBA ESTE ENCENDIDA
        if (orden.getDetalleOrden().getDetallesCarga().isEmpty()) {
            throw BusinessException.builder().message("Es encesario encender la bomba primero").build();
        }

        //VALIDA EL ESTADO
        if (orden.getEstado() != 2) {
            throw BusinessException.builder().message("La orden no se encuentra en proceso de carga").build();
        }

        // VALIDA QUE LOS DATOS DEL DETALLE SEAN CORRECTOS
        if (detalleCarga.getCaudal() <= 0 ||
                detalleCarga.getMasa() == 0 ||
                detalleCarga.getMasa() > orden.getPreset() ||
                detalleCarga.getMasa() < orden.getDetalleOrden().getDetallesCarga().get(orden.getDetalleOrden().getDetallesCarga().size() - 1).getMasa() ||
                detalleCarga.getDensidad() <= 0 && detalleCarga.getDensidad() >= 1) {
            throw BusinessException.builder().message("Los datos ingresados del detalle no son validos").build();
        }

        // VALIDAR SI ALCANZA EL PRESET -> FRENAR BOMBA
        if (detalleCarga.getMasa() == orden.getPreset()) {
            orden.setEstado(3);
            orden.getDetalleOrden().setFechaFinCarga(new Date());
        }

        // AGREGA EL NUEVO DETALLE A LA ORDEN
        detalleCarga.setFechaRecepcionCarga(new Date());
        orden.getDetalleOrden().getDetallesCarga().add(detalleCarga);
        update(orden);
    }

    @Override
    public Orden turnOffBomb(Long numero, int password) throws NotFoundException, BusinessException {
        Orden orden = load(numero);

        if (!orden.getPassword().equals(password)) {
            throw NotFoundException.builder().message("La password y el numero de orden no coinciden").build();
        }
        if (orden.getDetalleOrden().getDetallesCarga().isEmpty()) {
            throw BusinessException.builder().message("Es encesario encender la bomba primero").build();
        }
        if (orden.getEstado() != 2) {
            throw BusinessException.builder().message("La orden asociada no se encuentra en el estado requerido").build();
        }

            orden.setEstado(3);
            orden.getDetalleOrden().setFechaFinCarga(new Date());
            return update(orden);
}

    @Override
    public Orden cerrarOrden(Long numero) throws BusinessException, NotFoundException {
        Orden orden = load(numero);
        //VALIDA SI LA ORDEN ESTA EN ESTADO 3
        if(!orden.getEstado().equals(3)){
            throw BusinessException.builder().message("La orden asociada no se encuentra en el estado requerido").build();
        }
        orden.getDetalleOrden().setPesajeFinal(orden.getDetalleOrden().getPesajeInicial() +
                orden.getDetalleOrden().getDetallesCarga().get(orden.getDetalleOrden().getDetallesCarga().size()-1).getMasa());
        orden.getDetalleOrden().setFechaRecepcionFinal(new Date());
        orden.setEstado(4);
        update(orden);
        return concilacion(numero);
    }

    //TODO: ACA PODRIAMOS HACER UNA SLIMVIEW
    @Override
    public Orden concilacion(Long numero) throws BusinessException, NotFoundException {
        Orden orden = load(numero);
        //VALIDA SI LA ORDEN ESTA EN ESTADO 4
        if(!orden.getEstado().equals(4)){
            throw BusinessException.builder().message("La orden asociada no se encuentra en el estado requerido").build();
        }
        //TODO: HACER SERIALIZER
        return orden;
    }
}