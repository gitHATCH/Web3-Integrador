package org.efa.backend.model.business.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Camion;
import org.efa.backend.model.Cisternado;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.PasswordGenerator;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IOrdenBusiness;
import org.efa.backend.model.deserealizer.CheckInDeserealizer;
import org.efa.backend.model.deserealizer.CheckOutDesealizer;
import org.efa.backend.model.deserealizer.OrdenExternaDeserealizer;
import org.efa.backend.model.persistence.OrdenRepository;
import org.efa.backend.model.serializer.ConciliacionSerializer;
import org.efa.backend.utils.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdenBusiness implements IOrdenBusiness {

    @Autowired
    private DetalleBusiness detalleBusiness;
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private CamionBusiness camionBusiness;
    @Autowired
    private ChoferBusiness choferBusiness;
    @Autowired
    private CisternadoBusiness cisternadoBusiness;
    @Autowired
    private ClienteBusiness clienteBusiness;
    @Autowired
    private ProductoBusiness productoBusiness;
    @Autowired
    private MailBusiness mailBusiness;

    @Value("${temperatura.umbral}")
    private float temperaturaUmbral;

    @Override
    public String checkOut(String json, long numeroOrden) throws NotFoundException, BusinessException, JsonProcessingException {

        Orden orden = null;
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error al cargar la orden").build();
        }

        if (orden.getEstado() != 3) {
            throw BusinessException.builder().message("Error, primero realize el cierre de orden").build();
        }


        ObjectMapper mapper = JsonUtiles.getObjectMapper(Orden.class, new CheckOutDesealizer(Orden.class));
        Orden ordenNew;
        try {
            ordenNew = mapper.readValue(json, Orden.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        orden.setPesajeFinal(ordenNew.getPesajeFinal());
        orden.setFechaPesajeFinal(OffsetDateTime.now());
        orden.setEstado(4);
        ordenRepository.save(orden);
        StdSerializer<Orden> ser = new ConciliacionSerializer(Orden.class, detalleBusiness);
        return JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(orden);
    }

    @Override
    public String conciliacion(long numeroOrden) throws NotFoundException, JsonProcessingException, BusinessException {
        Orden orden = load(numeroOrden);
        if (orden.getEstado() == 4) {
            StdSerializer<Orden> ser = new ConciliacionSerializer(Orden.class, detalleBusiness);
            return JsonUtiles.getObjectMapper(Orden.class, ser, null).writeValueAsString(orden);
        } else {
            throw BusinessException.builder().message("Error, realize primero el check-out ").build();
        }
    }

    // obtengo la orden por el numero de orden
    @Override
    public Orden load(long numeroOrden) throws BusinessException, NotFoundException {
        Optional<Orden> r;
        try {
            r = ordenRepository.findByNumeroOrden(numeroOrden);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra la orden con id " + numeroOrden).build();
        }
        return r.get();
    }

    /**
     * Este método se utiliza para obtener todas las instancias de 'Orden' de la base de datos.
     * Utiliza el método 'findAll' del 'ordenRepository' para recuperar todas las instancias de 'Orden'.
     * Si ocurre alguna excepción durante el proceso, registra el mensaje de error y lanza una BusinessException con un mensaje personalizado.
     *
     * @return Lista de todas las instancias de 'Orden' de la base de datos.
     * @throws BusinessException si ocurre alguna excepción durante el proceso.
     */
    @Override
    public List<Orden> list() throws BusinessException {
        try {
            return ordenRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().message("Error al traer lista de las Ordenes.").build();
        }
    }

    @Override
    public Orden add(Orden orden) throws BusinessException, FoundException, NotFoundException {
        //            load(orden.getNumeroOrden());
        Optional<Orden> r = ordenRepository.findByNumeroOrden(orden.getNumeroOrden());
        if (r.isPresent()){
            throw FoundException.builder().message("Ya hay una orden con el nro " + orden.getNumeroOrden()).build();
        }
//        Camion camion;
        try {
            if (!camionBusiness.exists(orden.getCamion().getCode())) {
                // cuando el camion es nuevo
                camionBusiness.add(orden.getCamion());
                for (Cisternado cisternado : orden.getCamion().getDatosCisterna()) {
                    cisternado.setCamion(orden.getCamion());
                    cisternadoBusiness.add(cisternado);
                }
            } else {
                // camion q ya existe
//                camion = camionBusiness.add(orden.getCamion());
//                orden.setCamion(camion);
                throw FoundException.builder().message("Ya existe un camion con el CODIGO: " + orden.getCamion().getCode()).build();
            }
        }
        catch (FoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw BusinessException.builder().message("Error al crear el camion, en orden.").build();
        }
        try {
            choferBusiness.add(orden.getChofer());
        }
        catch (FoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw BusinessException.builder().message("Error al crear el chofer, en orden.").build();
        }
        try {
            clienteBusiness.add(orden.getCliente());
        }
        catch (FoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw BusinessException.builder().message("Error al crear el cliente, en orden.").build();
        }
        try {
            productoBusiness.add(orden.getProducto());
        }
        catch (FoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw e;
//            throw BusinessException.builder().message("Error al crear el producto, en orden.").build();
        }
        try {
            orden.setEstado(1);
            if (orden.getCodigoExterno() == null) {
                orden.setCodigoExterno(System.currentTimeMillis() + "");
            }
            if (orden.getTemperaturaUmbral() == 0) {
                orden.setTemperaturaUmbral(temperaturaUmbral);
            }

            return ordenRepository.save(orden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error en creacion de la orden.").build();
        }

    }

    @Override
    public Orden addExternal(String json) throws BusinessException, FoundException, NotFoundException {
        ObjectMapper mapper = JsonUtiles.getObjectMapper(Orden.class, new OrdenExternaDeserealizer(Orden.class, camionBusiness, choferBusiness, clienteBusiness, productoBusiness));
        Orden orden;
        try {
            orden = mapper.readValue(json, Orden.class);
        } catch (JsonProcessingException e) {
            throw BusinessException.builder().ex(e).build();
        }
        Optional<Orden> r = ordenRepository.findByNumeroOrden(orden.getNumeroOrden());
        if (r.isPresent()){
            throw FoundException.builder().message("Ya hay una orden con el nro " + orden.getNumeroOrden()).build();
        }
        try {
            camionBusiness.load(orden.getCamion().getCode());
        }
        catch (Exception e) {
            throw e;
        }
        try {
            choferBusiness.load(orden.getChofer().getCode());
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw BusinessException.builder().message("Error al agregar el chofer, en orden.").build();
        }
        try {
            clienteBusiness.load(orden.getCliente().getCode());
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw BusinessException.builder().message("Error al agregar el cliente, en orden.").build();
        }
        try {
            productoBusiness.load(orden.getProducto().getCode());
        }
        catch (NotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw BusinessException.builder().message("Error al agregar el producto, en orden.").build();
        }
        try {
            orden.setEstado(1);
            if (orden.getCodigoExterno() == null) {
                orden.setCodigoExterno(System.currentTimeMillis() + "");
            }
            if (orden.getTemperaturaUmbral() == 0) {
                orden.setTemperaturaUmbral(temperaturaUmbral);
            }

            return ordenRepository.save(orden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error en creacion de la orden.").build();
        }
    }

    @Override
    public Orden checkIn(String json, long numeroOrden) throws BusinessException {
        Orden orden;
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error al cargar la orden").build();
        }

        if (orden.getEstado() != 1) {
            throw BusinessException.builder().message("La orden no esta en estado 1").build();
        }

        ObjectMapper mapper = JsonUtiles.getObjectMapper(Orden.class, new CheckInDeserealizer(Orden.class));
        Orden ordenNew;
        try {
            ordenNew = mapper.readValue(json, Orden.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        orden.setTara(ordenNew.getTara());
        orden.setFechaPesajeInicial(OffsetDateTime.now());
        orden.setPassword(PasswordGenerator.generateFiveDigitPassword());
        mailBusiness.sendPasswordToAll(orden.getPassword());
        orden.setEstado(2);
        return ordenRepository.save(orden);
    }

    @Override
    public Orden closeOrder(long numeroOrden) throws BusinessException {
        Orden orden;
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error al cargar la orden").build();
        }
        if (orden.getEstado() != 2) {
            throw BusinessException.builder().message("La orden no esta en estado 2").build();
        }
        orden.setEstado(3);
        return ordenRepository.save(orden);
    }

    @Override
    public void aceptarAlarma(long numeroOrden) throws BusinessException {
        Orden orden;
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error al cargar la orden").build();
        }
        orden.setAlarma(false);
        ordenRepository.save(orden);
    }

    @Override
    public void setTemperaturaUmbral(long numeroOrden, float temp) throws BusinessException {
        Orden orden;
        try {
            orden = load(numeroOrden);
        } catch (Exception e) {
            throw BusinessException.builder().message("Error al cargar la orden").build();
        }
        orden.setTemperaturaUmbral(temp);
        ordenRepository.save(orden);
    }
}
