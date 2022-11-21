package org.efa.backend.model.business;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.DetalleCarga;
import org.efa.backend.model.DetalleOrden;
import org.efa.backend.model.Orden;
import org.efa.backend.model.persistence.OrdenRepository;
import org.efa.backend.model.views.IConciliacionSlimView;
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
    public Orden addTara(Long numero, Float tara) throws NotFoundException, BusinessException {
        final int MINIMO = 99999;
        final int MAXIMO = 10000;

        Orden orden = load(numero);

        if (orden.getEstado() != 1) {
            throw BusinessException.builder().message("La orden especificada ya pertenece a un estado superior por lo que tiene asignada una tara").build();
        }

        orden.setDetalleOrden(new DetalleOrden());
        orden.getDetalleOrden().setPesajeInicial(tara);
        orden.setEstado(2);
        orden.getDetalleOrden().setDetallesCarga(null);
        orden.getDetalleOrden().setFechaRecepcionPesajeInicial(new Date());
        orden.setPassword((int) Math.floor(Math.random() * (MAXIMO - MINIMO + 1) + MINIMO));
        if(System.getenv("MAIL_USERNAME") != null){
            emailBusiness.sendSimpleMessage(System.getenv("MAIL_USERNAME"),"Clave generada exitosamente","Su pin generado es: '"+orden.getPassword()+"' . Por favor conservelo por seguridad!");
        }
        return ordenDAO.save(orden);
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
            if (orden.getCamion().getCodigo() != null) {
                try {
                    orden.setCamion(camion.load(orden.getCamion().getCodigo()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El camion de código " + orden.getCamion().getCodigo() + " no se encuentra").build();
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
            if (orden.getChofer().getCodigo() != null) {
                try {
                    orden.setChofer(chofer.load(orden.getChofer().getCodigo()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El chofer de código " + orden.getChofer().getCodigo() + " no se encuentra").build();
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
            if (orden.getCliente().getCodigo() != null) {
                try {
                    orden.setCliente(cliente.load(orden.getCliente().getCodigo()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El cliente de código " + orden.getCliente().getCodigo() + " no se encuentra").build();
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
            if (orden.getProducto().getCodigo() != null) {
                try {
                    orden.setProducto(producto.load(orden.getProducto().getCodigo()));
                } catch (NotFoundException e) {
                    log.error(e.getMessage(), e);
                    throw BusinessException.builder().message("El producto de código " + orden.getProducto().getCodigo() + " no se encuentra").build();
                }
            }
        }
    }

    @Override
    public Orden turnOnBomb(Long numero) throws NotFoundException, BusinessException {
        try {
            Orden orden = load(numero);
            if (orden.getEstado() != 2 || !orden.getDetalleOrden().getDetallesCarga().isEmpty()) {
                throw BusinessException.builder().message("La orden asociada ya se encuentra en proceso").build();
            }
            orden.getDetalleOrden().setFechaInicioCarga(new Date());
            DetalleCarga ultimoDetalleCarga = DetalleCarga.builder()
                    .fechaRecepcionCarga(new Date())
                    .masa(0F)
                    .caudal(0F)
                    .densidad(0F)
                    .temperatura(0F)
                    .build();

            orden.getDetalleOrden().getDetallesCarga().add(ultimoDetalleCarga);
            orden.getDetalleOrden().setUltimoDetalleCarga(ultimoDetalleCarga);
            return update(orden);
            } catch (NotFoundException e) {
                log.error(e.getMessage(), e);
                throw NotFoundException.builder().ex(e).build();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw BusinessException.builder().ex(e).build();
            }
    }

    @Override
    public DetalleCarga getCargaActual(long numero) throws BusinessException, NotFoundException { ///Serializer con ultimo detalle carga, estado y detallesCarga
        try {
            Orden orden = load(numero);
            if (orden.getEstado() != 2 || orden.getDetalleOrden().getDetallesCarga().isEmpty()) {
                throw BusinessException.builder().message("La orden correspondiente no se encuentra en proceso de carga").build();
            } else {
                return orden.getDetalleOrden().getUltimoDetalleCarga();
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
                detalleCarga.getMasa() < orden.getDetalleOrden().getUltimoDetalleCarga().getMasa() ||
                detalleCarga.getDensidad() <= 0 && detalleCarga.getDensidad() >= 1) {
            throw BusinessException.builder().message("Los datos ingresados del detalle no son validos").build();
        }

        // VALIDAR SI ALCANZA EL PRESET -> FRENAR BOMBA
        if (Objects.equals(detalleCarga.getMasa(), orden.getPreset())) {
            orden.setEstado(3);
            orden.getDetalleOrden().setFechaFinCarga(new Date());
        }

        // AGREGA EL NUEVO DETALLE A LA ORDEN
        detalleCarga.setFechaRecepcionCarga(new Date());
        orden.getDetalleOrden().getDetallesCarga().add(detalleCarga);
        orden.getDetalleOrden().setUltimoDetalleCarga(detalleCarga);
        update(orden);
    }

    @Override
    public Orden turnOffBomb(Long numero) throws NotFoundException, BusinessException {
        Orden orden = load(numero);

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
    public IConciliacionSlimView cerrarOrden(Long numero) throws BusinessException, NotFoundException {
        Orden orden = load(numero);
        //VALIDA SI LA ORDEN ESTA EN ESTADO 3
        if(!orden.getEstado().equals(3)){
            throw BusinessException.builder().message("La orden asociada no se encuentra en el estado requerido").build();
        }
        Float pesoFinal = orden.getDetalleOrden().getUltimoDetalleCarga().getMasa() + orden.getDetalleOrden().getPesajeInicial();
        orden.getDetalleOrden().setPesajeFinal(pesoFinal);
        orden.getDetalleOrden().setFechaRecepcionFinal(new Date());
        orden.setEstado(4);
        update(orden);
        return concilacion(numero);
    }

    @Override
    public IConciliacionSlimView concilacion(Long numero) throws BusinessException, NotFoundException {///Serializer con ultimo detalle carga y estado
        Orden orden = load(numero);
        //VALIDA SI LA ORDEN ESTA EN ESTADO 4
        if(!orden.getEstado().equals(4)){
            throw BusinessException.builder().message("La orden asociada no se encuentra en el estado requerido").build();
        }
        IConciliacionSlimView conciliacion = ordenDAO.getConciliacion(numero);;

        return conciliacion;
    }

//    @Override
//    public Orden addExternal(String json) throws FoundException, BusinessException {
//        ObjectMapper mapper = JsonUtiles.getObjectMapper(Orden.class,
//                new OrdenJSONDesserializer(Orden.class, camion, chofer, cliente, producto));
//        Orden orden;
//        try {
//            orden = mapper.readValue(json, Orden.class);
//        } catch (JsonProcessingException e) {
//            log.error(e.getMessage(), e);
//            throw BusinessException.builder().ex(e).build();
//        }
//        return add(orden);
//    }
}