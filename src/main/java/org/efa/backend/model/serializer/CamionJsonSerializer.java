package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Camion;
import org.efa.backend.model.Cisternado;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;

public class CamionJsonSerializer extends StdSerializer<Camion> {

    private static final long serialVersionUID = -4375128170177242893L;

    public CamionJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Camion camion, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("patente", camion.getPatente());
        jsonGenerator.writeStringField("descripcion", camion.getDescripcion());

        String cisterna = JsonUtiles
                .getObjectMapper(Cisternado.class, new CisternadoJsonSerializer(Cisternado.class, false), null)
                .writeValueAsString(camion.getDatosCisterna());
        jsonGenerator.writeFieldName("datosCisterna");
        jsonGenerator.writeRawValue(cisterna);

        jsonGenerator.writeNumberField("totalCisterna", camion.getTotalCisterna());
        jsonGenerator.writeStringField("code", camion.getCode());
        jsonGenerator.writeEndObject();

    }
}
