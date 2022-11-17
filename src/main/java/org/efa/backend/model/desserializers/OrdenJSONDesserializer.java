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

        String codigo = JsonUtiles.getString(node, "code_externo,cexterno_code,code,codigoExterno".split(","),null);

        Long numero = Long.parseLong(JsonUtiles.getString(node,
                "number,pedido,track_number,numero".split(","), "0"));
        Float preset = Float.parseFloat(JsonUtiles.getString(node, "limite,preset".split(","), "0"));

        String codigoCamion = JsonUtiles.getString(node,
                "camion,codigo_camion,codigoCamion,codigoCam".split(","), null);

        String codigoChofer = JsonUtiles.getString(node,
                "chofer,codigo_chofer,codigoChofer,codigoCho".split(","), null);

        String codigoCliente = JsonUtiles.getString(node,
                "cliente,codigo_cliente,codigoCliente,codigoCli".split(","), null);

        String codigoProducto = JsonUtiles.getString(node,
                "producto,codigo_producto,codigoProducto,codigoPro".split(","), null);

        String fechaPrevista = JsonUtiles.getString(node,
                "fecha_prevista,fecha,fecha_carga_prevista".split(","), null);

            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            fecha = formato.parse(fechaPrevista);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if(numero != 0 && preset != 0 && codigo != null){
            r.setNumero(numero);
            r.setPreset(preset);
            r.setCodigo(codigo);
            r.setFechaCargaPrevista(fecha);
        }else{
            throw new RuntimeException("Datos no válidos");
        }

        try {
            r.setCamion(camionBusiness.load(codigoCamion));
            r.setChofer(choferBusiness.load(codigoChofer));
            r.setCliente(clienteBusiness.load(codigoCliente));
            r.setProducto(productoBusiness.load(codigoProducto));
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        } catch (NotFoundException e){
            throw new RuntimeException(e);
        }

        return r;
    }
}
