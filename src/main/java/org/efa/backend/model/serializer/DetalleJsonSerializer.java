package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Detalle;
import org.efa.backend.model.Orden;
import org.efa.backend.model.business.implementations.OrdenBusiness;
import org.efa.backend.model.business.interfaces.IOrdenBusiness;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class DetalleJsonSerializer extends StdSerializer<Detalle> {

    public DetalleJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    private Orden orden = null;

    public DetalleJsonSerializer(Class<?> t, boolean dummy, Orden orden) {
        super(t, dummy);
        this.orden = orden;
    }
    @Override
    public void serialize(Detalle detalle, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("masa", detalle.getMasa());
        gen.writeNumberField("densidad", detalle.getDensidad());
        gen.writeNumberField("temperatura", detalle.getTemperatura());
        gen.writeNumberField("caudal", detalle.getCaudal());
        gen.writeStringField("fechaDetalle", detalle.getFechaDetalle() != null ? detalle.getFechaDetalle().toString() : "");

        if (this.orden != null) {

            double masa = detalle.getMasa();
            double caudal = detalle.getCaudal();
            double masaRestante = this.orden.getPreset() - masa;
            double eta = caudal > 0 ? masaRestante / caudal : Double.POSITIVE_INFINITY;
            double horas = Math.floor(eta / 3600);
            double minutos = Math.floor((eta % 3600) / 60);
            double segundos = Math.floor(eta % 60);
            String tiempoEstimadoCarga = String.format("%02d:%02d:%02d", (int) horas, (int) minutos, (int) segundos);
            gen.writeStringField("tiempoEstimadoCarga", tiempoEstimadoCarga);
            //gen.writeNumberField("estado", value.getEstado());

            double fechaDetalleFinal = detalle.getFechaDetalle() != null ? detalle.getFechaDetalle().toEpochSecond() : 0;
            double fechaDetalleInicial = orden.getFechaDetalleInicial() != null ? orden.getFechaDetalleInicial().toEpochSecond() : 0;
            double tiempo = fechaDetalleFinal - fechaDetalleInicial;
            double segundosTranscurridos = tiempo % 60;
            double minutosTranscurridos = (tiempo / 60) % 60;
            double horasTranscurridas = (tiempo / 3600) % 24;
            String tiempoTranscurrido = String.format("%02d:%02d:%02d", (int) horasTranscurridas, (int) minutosTranscurridos, (int) segundosTranscurridos);
            gen.writeStringField("tiempoTranscurrido", tiempoTranscurrido);
        }
        gen.writeEndObject();

    }
}
