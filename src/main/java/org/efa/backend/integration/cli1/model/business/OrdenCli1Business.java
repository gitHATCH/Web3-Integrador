package org.efa.backend.integration.cli1.model.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.FoundException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.integration.cli1.model.OrdenCli1;
import org.efa.backend.integration.cli1.model.deserializers.OrdenCli1JSONDeserializer;
import org.efa.backend.integration.cli1.model.persistence.OrdenCli1Repository;
import org.efa.backend.model.business.CamionBusiness;
import org.efa.backend.model.business.ChoferBusiness;
import org.efa.backend.model.business.ClienteBusiness;
import org.efa.backend.model.business.ProductoBusiness;
import org.efa.backend.utils.JsonUtiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrdenCli1Business implements IOrdenCli1Business {

    @Autowired
    private OrdenCli1Repository ordenCli1DAO;

    @Override
    public OrdenCli1 load(String codigo) throws NotFoundException, BusinessException {
        Optional<OrdenCli1> r;
        try {
            r = ordenCli1DAO.findByCodigo(codigo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra la ordenCli1 con codigo " + codigo).build();
        }
        return r.get();
    }

    @Override
    public List<OrdenCli1> list() throws BusinessException {
        try {
            return ordenCli1DAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public OrdenCli1 add(OrdenCli1 ordenCli1) throws FoundException, BusinessException {
        try {
            load(ordenCli1.getCodigo());
            throw FoundException.builder().message("Se encontró la ordenCli1 con codigo " + ordenCli1.getCodigo()).build();
        } catch (NotFoundException e) {
        }
        ordenCli1.setEstado(1);
        ordenCli1.setDetalleOrden(null);
        try {
            return ordenCli1DAO.save(ordenCli1);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Autowired
    private CamionBusiness camionBusiness;
    @Autowired
    private ChoferBusiness choferBusiness;
    @Autowired
    private ProductoBusiness productoBusiness;
    @Autowired
    private ClienteBusiness clienteBusiness;

    @Override
    public OrdenCli1 addExternal(String json) throws FoundException, BusinessException {
        ObjectMapper mapper = JsonUtiles.getObjectMapper(OrdenCli1.class, new OrdenCli1JSONDeserializer(OrdenCli1.class,
                camionBusiness , choferBusiness, clienteBusiness, productoBusiness));
        OrdenCli1 orden;

        try {
            orden = mapper.readValue(json, OrdenCli1.class);
            orden.setEstado(1);
            orden.setDetalleOrden(null);
        }catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }

        return add(orden);

    }

}
