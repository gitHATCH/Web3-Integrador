package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Detalle;

import java.io.IOException;

public class DetalleJsonSerializer extends StdSerializer<Detalle> {

    public DetalleJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Detalle detalle, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("masa", detalle.getMasa());
        gen.writeNumberField("densidad", detalle.getDensidad());
        gen.writeNumberField("temperatura", detalle.getTemperatura());
        gen.writeNumberField("caudal", detalle.getCaudal());

        gen.writeEndObject();

    }
}
