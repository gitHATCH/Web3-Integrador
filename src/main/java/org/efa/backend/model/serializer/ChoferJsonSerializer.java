package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Chofer;

import java.io.IOException;

public class ChoferJsonSerializer extends StdSerializer<Chofer> {

    private static final long serialVersionUID = -6418296775344118648L;

    public ChoferJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Chofer chofer, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("dni", chofer.getDni());
        jsonGenerator.writeStringField("nombre", chofer.getNombre());
        jsonGenerator.writeStringField("apellido", chofer.getApellido());
        jsonGenerator.writeStringField("code", chofer.getCode());
        jsonGenerator.writeEndObject();
    }
}
