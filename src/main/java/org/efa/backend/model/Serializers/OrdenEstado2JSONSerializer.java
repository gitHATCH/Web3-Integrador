package org.efa.backend.model.Serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Orden;

import java.io.IOException;

public class OrdenEstado2JSONSerializer extends StdSerializer<Orden> {
    public OrdenEstado2JSONSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Orden orden, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("Numero orden", orden.getNumero());
        gen.writeNumberField("Password", orden.getPassword());
        gen.writeNumberField("Preset", orden.getPreset());

        gen.writeEndObject();
    }
}
