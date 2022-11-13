package org.efa.backend.model.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Orden;
import org.efa.backend.model.views.Conciliacion;
import org.efa.backend.model.views.IConciliacionSlimView;

import java.io.IOException;

public class ConciliacionJSONSerializer extends StdSerializer<Conciliacion> {

    public ConciliacionJSONSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Conciliacion value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("Numero de Orden", value.getNumeroOrden());
        gen.writeNumberField("Pesaje inicial", value.getPesajeIncial());
        gen.writeNumberField("Pesaje final", value.getPesajeFinal());
        gen.writeNumberField("Masa", value.getMasa());
        gen.writeNumberField("Neto por balanza", value.getPesajeFinal() - value.getPesajeIncial());
        gen.writeNumberField("Diferencia balanza-caudalimetro", (value.getPesajeFinal() - value.getPesajeIncial()) - value.getMasa());
        gen.writeNumberField("Promedio de temperatura", value.getPromedioTemperatura());
        gen.writeNumberField("Promedio de densidad", value.getPromedioDensidad());
        gen.writeNumberField("Promedio de caudal", value.getPromedioCaudal());

        gen.writeEndObject();
    }
}
//Diferencia balanza-caudalimetro (Neto x balanza - getMasa)