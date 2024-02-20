package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Producto;

import java.io.IOException;

public class ProductoJsonSerializer extends StdSerializer<Producto> {

    private static final long serialVersionUID = 2319513962332548817L;

    public ProductoJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Producto producto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
//        jsonGenerator.writeNumberField("id", producto.getId());
        jsonGenerator.writeStringField("nombre", producto.getNombre());
        jsonGenerator.writeStringField("descripcion", producto.getDescripcion());
        jsonGenerator.writeStringField("code", producto.getCode());
        jsonGenerator.writeEndObject();
    }
}
