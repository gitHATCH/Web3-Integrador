package org.efa.backend.model.desserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.*;
import org.efa.backend.utils.JsonUtiles;


public class OrdenJSONDesserializer extends StdDeserializer<Orden> {

    private static final long serialVersionUID = -3881285352118964728L;

    protected OrdenJSONDesserializer(Class<?> vc) {
        super(vc);
    }

    private ICamionBusiness camionBusiness;

    private IChoferBusiness choferBusiness;

    private IClienteBusiness clienteBusiness;

    private IProductoBusiness productoBusiness;

    public OrdenJSONDesserializer(Class<?> vc, ICamionBusiness camionBusiness, IChoferBusiness choferBusiness, IClienteBusiness clienteBusiness, IProductoBusiness productoBusiness) {
        super(vc);
        this.camionBusiness = camionBusiness;
        this.choferBusiness = choferBusiness;
        this.clienteBusiness = clienteBusiness;
        this.productoBusiness = productoBusiness;
    }

    @Override
    public Orden deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        Orden r = new Orden();
        JsonNode node = jp.getCodec().readTree(jp);

        String codigoexterno = JsonUtiles.getString(node, "code_externo,cexterno_code,code,codigoExterno".split(","),
                System.currentTimeMillis() + "");
        Long numero = JsonUtiles.getLong(node,
                "number,pedido,track_number,numero".split(","), 0);
        int estado = JsonUtiles.getInteger(node, ("status,situacion,circunstancia,estado").split(","),0);
        float preset = JsonUtiles.getFloat(node, "limite,preset".split(","), 0);
        Integer password = JsonUtiles.getInteger(node, "contrasenia,code_access, access_code,password,pin,pass,contraseña".split(","), 0);
        Long camion = JsonUtiles.getLong(node,
                "truck,autocamion,camion,idcamion,id_camion,idCamion".split(","), 0);
        String camion_patente = JsonUtiles.getString(node,
                "patente,patentecamion,patenteCamion,camionpatente,camionPatente,patente_camion,camion_patente".split(","), null);
        Long chofer = JsonUtiles.getLong(node,
                "chofer,choffer,id_chofer,idchofer,idChofer".split(","), 0);
        Long cliente = JsonUtiles.getLong(node,
                "persona,cliente,consumidor".split(","), 0);
        Long producto = JsonUtiles.getLong(node,
                "articulo,producto,produccion".split(","), 0);

        r.setEstado(estado);
        r.setNumero(numero);
        r.setPreset(preset);
        r.setPassword(password);
        r.setCodigoExterno(codigoexterno);

        if (camion != 0) {
            try {
                r.setCamion(camionBusiness.loadById(camion));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if(camion_patente!=null){
            try {
                r.setCamion(camionBusiness.load(camion_patente));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (chofer != 0) {
            try {
                r.setChofer(choferBusiness.loadById(chofer));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (cliente != 0) {
            try {
                r.setCliente(clienteBusiness.loadById(cliente));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        if (producto != 0) {
            try {
                r.setProducto(productoBusiness.loadById(producto));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return r;
    }
}
