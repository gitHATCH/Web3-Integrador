package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.*;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;

public class OrdenJsonSerializer extends StdSerializer<Orden> {

    private static final long serialVersionUID = -3915921537879917268L;

    public OrdenJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Orden value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("numeroOrden", value.getNumeroOrden());

        String camion = JsonUtiles
                .getObjectMapper(Camion.class, new CamionJsonSerializer(Camion.class, false), null)
                .writeValueAsString(value.getCamion());
        gen.writeFieldName("camion");
        gen.writeRawValue(camion);

        String chofer = JsonUtiles
                .getObjectMapper(Chofer.class, new ChoferJsonSerializer(Chofer.class, false), null)
                .writeValueAsString(value.getChofer());
        gen.writeFieldName("chofer");
        gen.writeRawValue(chofer);

        String cliente = JsonUtiles
                .getObjectMapper(Cliente.class, new ClienteJsonSerializer(Cliente.class, false), null)
                .writeValueAsString(value.getCliente());
        gen.writeFieldName("cliente");
        gen.writeRawValue(cliente);

        String producto = JsonUtiles
                .getObjectMapper(Producto.class, new ProductoJsonSerializer(Producto.class, false), null)
                .writeValueAsString(value.getProducto());
        gen.writeFieldName("producto");
        gen.writeRawValue(producto);

        gen.writeNumberField("preset", value.getPreset());
        gen.writeStringField("fechaTurnoCarga", value.getFechaTurnoCarga().toString());
        gen.writeStringField("codigoExterno", value.getCodigoExterno());
//        double masa = value.getUltimaMasa();
//        double caudal = value.getUltimoCaudal();
//        double masaRestante = value.getPreset() - masa;
//        double eta = caudal > 0 ? masaRestante / caudal : Double.POSITIVE_INFINITY;
//        double horas = Math.floor(eta/3600);
//        double minutos = Math.floor((eta%3600)/60);
//        double segundos = Math.floor(eta%60);
//        String tiempoEstimadoCarga = String.format("%02d:%02d:%02d", (int)horas, (int)minutos, (int)segundos);
//        gen.writeStringField("tiempoEstimadoCarga", tiempoEstimadoCarga);
//        //gen.writeNumberField("estado", value.getEstado());
//
//        gen.writeObjectField("detalle",value.getDetalle());

    }
}

/**
 "preset": 1540.0,
 "estado": 2,
 "tara": 25000,
 "alarma": true,
 "temperaturaUmbral": 40.0,
 "ultimaMasa": 700.0,
 "ultimaDensidad": 0.9,
 "ultimaTemperatura": 50.0,
 "ultimoCaudal": 26.0,
 "pesajeFinal": 0,
 "fechaDetalleFinal": "2024-02-20T01:32:55.079882-03:00",
 "fechaDetalleInicial": "2024-02-20T01:32:40.109536-03:00",
 "codigoExterno": "code123"
 * **/