package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Cisternado;

import java.io.IOException;

public class CisternadoJsonSerializer extends StdSerializer<Cisternado> {

    private static final long serialVersionUID = -2386228774048619911L;

    public CisternadoJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Cisternado cisternado, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("tamanio", cisternado.getTamanio());
        jsonGenerator.writeEndObject();
    }
}
