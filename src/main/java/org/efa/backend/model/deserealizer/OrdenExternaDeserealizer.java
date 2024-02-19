package org.efa.backend.model.deserealizer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.efa.backend.model.Camion;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.ICamionBusiness;
import org.efa.backend.model.business.interfaces.IChoferBusiness;
import org.efa.backend.model.business.interfaces.IClienteBusiness;
import org.efa.backend.model.business.interfaces.IProductoBusiness;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;
import java.time.OffsetDateTime;

public class OrdenExternaDeserealizer extends StdDeserializer<Orden> {

    protected OrdenExternaDeserealizer(Class<?> src) {
        super(src);
    }

    private ICamionBusiness camionBusiness;
    private IChoferBusiness choferBusiness;
    private IClienteBusiness clienteBusiness;
    private IProductoBusiness productoBusiness;

    public OrdenExternaDeserealizer(Class<?> src, ICamionBusiness camionBusiness, IChoferBusiness choferBusiness, IClienteBusiness clienteBusiness, IProductoBusiness productoBusiness) {
        super(src);
        this.camionBusiness = camionBusiness;
        this.choferBusiness = choferBusiness;
        this.clienteBusiness = clienteBusiness;
        this.productoBusiness = productoBusiness;
    }

    @Override
    public Orden deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.readValueAsTree();

        String numeroOrden = JsonUtiles.getString(node, "numeroOrden,numero-orden".split(","), null);
        String codigoExterno = JsonUtiles.getString(node, "codigoExterno,codigo,codigoExternoOrden,codigo-externo-orden,codigo-externo,ordenCodigoExterno,orden-codigo-externo".split(","), System.currentTimeMillis() + "");
        String camion = JsonUtiles.getString(node, "camion,camionCode,camion-code".split(","), null);
        String chofer = JsonUtiles.getString(node, "chofer,choferCode,chofer-code".split(","), null);
        String cliente = JsonUtiles.getString(node, "cliente,clienteCode,cliente-code".split(","), null);
        String producto = JsonUtiles.getString(node, "producto,productoCode,producto-code".split(","), null);
        String preset = JsonUtiles.getString(node, "preset".split(","), null);
        String fechaTurnoCarga = JsonUtiles.getString(node, "fechaTurnoCarga,fecha-turno-carga".split(","), null);

        Orden r = new Orden();
        r.setNumeroOrden(Long.parseLong(numeroOrden));
        if (camion != null) {
            try {
                Camion camion1 = camionBusiness.load(camion);
                r.setCamion(camion1); //busca por codigo de camion
            } catch (FoundException | BusinessException | NotFoundException e) {
                System.out.println("Error: " + e);
                throw new IOException(e);
            }
        }
        if (chofer != null) {
            try {
                r.setChofer(choferBusiness.load(chofer)); //busca por codigo de chofer
            } catch (BusinessException | NotFoundException e) {
            }
        }
        if (cliente != null) {
            try {
                r.setCliente(clienteBusiness.load(cliente)); //busca por codigo de cliente
            } catch (FoundException | BusinessException | NotFoundException e) {
            }
        }
        if (producto != null) {
            try {
                r.setProducto(productoBusiness.load(producto)); //busca por codigo de producto
            } catch (BusinessException | NotFoundException e) {
            }
        }
        r.setPreset(Float.parseFloat(preset));
        r.setFechaTurnoCarga(OffsetDateTime.parse(fechaTurnoCarga));
        r.setCodigoExterno(codigoExterno);

        return r;
    }
}
