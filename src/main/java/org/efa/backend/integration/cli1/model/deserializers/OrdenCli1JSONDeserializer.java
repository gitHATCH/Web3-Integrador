package org.efa.backend.integration.cli1.model.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.efa.backend.exceptions.custom.BusinessException;
import org.efa.backend.exceptions.custom.NotFoundException;
import org.efa.backend.integration.cli1.model.OrdenCli1;
import org.efa.backend.model.business.ICamionBusiness;
import org.efa.backend.model.business.IChoferBusiness;
import org.efa.backend.model.business.IClienteBusiness;
import org.efa.backend.model.business.IProductoBusiness;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrdenCli1JSONDeserializer  extends StdDeserializer<OrdenCli1> {
    private static final long serialVersionUID = -3881285352118964728L;

    private final ICamionBusiness camionBusiness;

    private final IChoferBusiness choferBusiness;

    private final IClienteBusiness clienteBusiness;

    private final IProductoBusiness productoBusiness;

    public OrdenCli1JSONDeserializer(Class<?> vc, ICamionBusiness camionBusiness, IChoferBusiness choferBusiness, IClienteBusiness clienteBusiness, IProductoBusiness productoBusiness) {
        super(vc);
        this.camionBusiness = camionBusiness;
        this.choferBusiness = choferBusiness;
        this.clienteBusiness = clienteBusiness;
        this.productoBusiness = productoBusiness;
    }

    @Override
    public OrdenCli1 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        OrdenCli1 r = new OrdenCli1();
        Date fecha;
        JsonNode node = jp.getCodec().readTree(jp);

        String codigo = JsonUtiles.getString(node, "code_externo,cexterno_code,code,codigoExterno".split(","),null);

        long numero = Long.parseLong(JsonUtiles.getString(node,
                "number,pedido,track_number,numero".split(","), "0"));
        float preset = Float.parseFloat(JsonUtiles.getString(node, "limite,preset".split(","), "0"));

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
