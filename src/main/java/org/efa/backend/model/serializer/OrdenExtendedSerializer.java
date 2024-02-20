package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.*;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;

public class OrdenExtendedSerializer extends StdSerializer<Orden> {

    private static final long serialVersionUID = -3915921537879917268L;

    public OrdenExtendedSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Orden value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeNumberField("numeroOrden", value.getNumeroOrden());
        gen.writeStringField("patenteCamion", value.getCamion().getPatente());
        gen.writeNumberField("dniChofer", value.getChofer().getDni());
        gen.writeStringField("razonSocialCliente", value.getCliente().getRazonSocial());
        gen.writeStringField("nombreProducto", value.getProducto().getNombre());

        StdSerializer<Detalle> detalleStdSerializer = new DetalleJsonSerializer(Detalle.class, false, value);
        String detalle = JsonUtiles.getObjectMapper(Detalle.class, detalleStdSerializer, null).writeValueAsString(value.getDetalle());
        gen.writeFieldName("detalle");
        gen.writeRawValue(detalle);

        gen.writeNumberField("preset", value.getPreset());
        gen.writeNumberField("estado", value.getEstado());
        gen.writeNumberField("tara", value.getTara());
        gen.writeBooleanField("alarma", value.isAlarma());
        gen.writeNumberField("temperaturaUmbral", value.getTemperaturaUmbral());
        gen.writeNumberField("ultimaMasa", value.getUltimaMasa());
        gen.writeNumberField("ultimaDensidad", value.getUltimaDensidad());
        gen.writeNumberField("ultimaTemperatura", value.getUltimaTemperatura());
        gen.writeNumberField("ultimoCaudal", value.getUltimoCaudal());

        double masa = value.getUltimaMasa();
        double caudal = value.getUltimoCaudal();
        double masaRestante = value.getPreset() - masa;
        double eta = caudal > 0 ? masaRestante / caudal : Double.POSITIVE_INFINITY;
        double horas = Math.floor(eta/3600);
        double minutos = Math.floor((eta%3600)/60);
        double segundos = Math.floor(eta%60);
        String tiempoEstimadoCarga = String.format("%02d:%02d:%02d", (int)horas, (int)minutos, (int)segundos);
        gen.writeStringField("tiempoEstimadoCarga", tiempoEstimadoCarga);

        double fechaDetalleFinal = value.getFechaDetalleFinal() != null ? value.getFechaDetalleFinal().toEpochSecond() : 0;
        double fechaDetalleInicial = value.getFechaDetalleInicial() != null ? value.getFechaDetalleInicial().toEpochSecond() : 0;
        double tiempo = fechaDetalleFinal - fechaDetalleInicial;
        double segundosTranscurridos = tiempo % 60;
        double minutosTranscurridos = (tiempo / 60) % 60;
        double horasTranscurridas = (tiempo / 3600) % 24;
        String tiempoTranscurrido = String.format("%02d:%02d:%02d", (int)horasTranscurridas, (int)minutosTranscurridos, (int)segundosTranscurridos);
        gen.writeStringField("tiempoTranscurrido", tiempoTranscurrido);
        
        
        gen.writeEndObject();

    }
}