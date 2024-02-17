package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Detalle;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.implementations.DetalleBusiness;

import java.io.IOException;
import java.util.List;

public class ConciliacionSerializer extends StdSerializer<Orden> {

    DetalleBusiness detalleBusiness;

    protected ConciliacionSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    public ConciliacionSerializer(Class<?> t, DetalleBusiness detalleBusiness) {
        super((Class<Orden>) t);
        this.detalleBusiness = detalleBusiness;
    }

    @Override
    public void serialize(Orden orden, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("pesaje_inicial", orden.getTara());
        gen.writeNumberField("pesaje_final", orden.getPesajeFinal());
        List<Detalle> detalles;
        try {
            detalles = detalleBusiness.listByNumeroOrden(orden.getNumeroOrden());
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        float masa = 0, tempProm = 0, densProm = 0, cauProm = 0;
        for (Detalle detalle : detalles) {
            if (masa < detalle.getMasa()) {
                masa = detalle.getMasa();
            }
            tempProm += detalle.getTemperatura();
            densProm += detalle.getDensidad();
            cauProm += detalle.getCaudal();
        }

        float netoBalanza = orden.getPesajeFinal() - orden.getTara();

        gen.writeNumberField("producto_cargado", masa);
        gen.writeNumberField("neto_por_balanza", netoBalanza);
        gen.writeNumberField("diferencia_balanza_caudalimetro", netoBalanza - masa);
        gen.writeNumberField("promedio_temperatura", tempProm / detalles.size());
        gen.writeNumberField("promedio_densidad", densProm / detalles.size());
        gen.writeNumberField("promedio_caudal", cauProm / detalles.size());
        gen.writeEndObject();


    }
}
