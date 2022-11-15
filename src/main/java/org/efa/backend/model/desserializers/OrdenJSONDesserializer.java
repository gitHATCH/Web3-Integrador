package org.efa.backend.model.desserializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        Date fecha;
        JsonNode node = jp.getCodec().readTree(jp);

        String codigoexterno = JsonUtiles.getString(node, "code_externo,cexterno_code,code,codigoExterno".split(","),null);

        Long numero = Long.parseLong(JsonUtiles.getString(node,
                "number,pedido,track_number,numero".split(","), "0"));
        Float preset = Float.parseFloat(JsonUtiles.getString(node, "limite,preset".split(","), "0"));
        Long camion = Long.parseLong(JsonUtiles.getString(node,
                "truck,autocamion,camion,idcamion,id_camion,idCamion".split(","), "0"));

        String camion_patente = JsonUtiles.getString(node,
                "patente,patentecamion,patenteCamion,camionpatente,camionPatente,patente_camion,camion_patente".split(","), null);
        Long chofer = Long.parseLong(JsonUtiles.getString(node,
                "chofer,choffer,id_chofer,idchofer,idChofer".split(","), "0"));
        Long dni_chofer = Long.parseLong(JsonUtiles.getString(node,
                "dni,dniChofer,dni_chofer".split(","), "0"));
        Long cliente = Long.parseLong(JsonUtiles.getString(node,
                "persona,cliente,consumidor".split(","), "0"));
        String razon_social = JsonUtiles.getString(node,
                "razon_social,razonSocial,razonSocialCliente,razon_social_cliente".split(","), null);
        Long producto = Long.parseLong(JsonUtiles.getString(node,
                "articulo,producto,produccion".split(","), "0"));
        String nombre = JsonUtiles.getString(node,
                "nombre,nombreProducto,nombre_producto".split(","), null);
        String fechaPrevista = JsonUtiles.getString(node,
                "fecha_prevista,fecha,fecha_carga_prevista".split(","), null);

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            fecha = formato.parse(fechaPrevista);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }



        r.setNumero(numero);
        r.setPreset(preset);
        r.setCodigoExterno(codigoexterno);
        r.setFechaCargaPrevista(fecha);

        if (camion != 0) {
            try {
                r.setCamion(camionBusiness.loadById(camion));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if(camion_patente != null){
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

        if (dni_chofer != null) {
            try {
                r.setChofer(choferBusiness.load(dni_chofer));
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

        if (razon_social != null) {
            try {
                r.setCliente(clienteBusiness.load(razon_social));
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

        if (nombre != null) {
            try {
                r.setProducto(productoBusiness.load(nombre));
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return r;
    }
}
