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

    }
}