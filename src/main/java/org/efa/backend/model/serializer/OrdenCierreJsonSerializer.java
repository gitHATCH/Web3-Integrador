package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Orden;

import java.io.IOException;

public class OrdenCierreJsonSerializer extends StdSerializer<Orden> {

    private static final long serialVersionUID = -250338667567740731L;

    public OrdenCierreJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }


    @Override
    public void serialize(Orden orden, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("detalle", "orden cerrada");
        // sacar para afuera la fecha que se necesite en este punto.

        gen.writeEndObject();
    }
}
