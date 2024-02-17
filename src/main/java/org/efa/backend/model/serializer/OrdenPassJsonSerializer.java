package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.*;

import java.io.IOException;

public class OrdenPassJsonSerializer extends StdSerializer<Orden> {
    public OrdenPassJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Orden value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("password", value.getPassword());
        //gen.writeStringField("fechaPesajeInicial", value.getFechaPesajeInicial().toString());

        gen.writeEndObject();
    }
}
