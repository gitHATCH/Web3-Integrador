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

        StdSerializer<Camion> camionStdSerializer = new CamionJsonSerializer(Camion.class, false);
        String camion = JsonUtiles.getObjectMapper(Camion.class, camionStdSerializer, null).writeValueAsString(value.getCamion());
//        String camion = JsonUtiles
//                .getObjectMapper(Camion.class, new CamionJsonSerializer(Camion.class, false), null)
//                .writeValueAsString(value.getCamion());
        gen.writeFieldName("camion");
        gen.writeRawValue(camion);

        StdSerializer<Chofer> choferStdSerializer = new ChoferJsonSerializer(Chofer.class, false);
        String chofer = JsonUtiles.getObjectMapper(Chofer.class, choferStdSerializer, null).writeValueAsString(value.getChofer());
//        String chofer = JsonUtiles
//                .getObjectMapper(Chofer.class, new ChoferJsonSerializer(Chofer.class, false), null)
//                .writeValueAsString(value.getChofer());
        gen.writeFieldName("chofer");
        gen.writeRawValue(chofer);

        StdSerializer<Cliente> clienteStdSerializer = new ClienteJsonSerializer(Cliente.class, false);
        String cliente = JsonUtiles.getObjectMapper(Cliente.class, clienteStdSerializer, null).writeValueAsString(value.getCliente());
//        String cliente = JsonUtiles
//                .getObjectMapper(Cliente.class, new ClienteJsonSerializer(Cliente.class, false), null)
//                .writeValueAsString(value.getCliente());
        gen.writeFieldName("cliente");
        gen.writeRawValue(cliente);

        StdSerializer<Producto> productoStdSerializer = new ProductoJsonSerializer(Producto.class, false);
        String producto = JsonUtiles.getObjectMapper(Producto.class, productoStdSerializer, null).writeValueAsString(value.getProducto());
//        String producto = JsonUtiles
//                .getObjectMapper(Producto.class, new ProductoJsonSerializer(Producto.class, false), null)
//                .writeValueAsString(value.getProducto());
        gen.writeFieldName("producto");
        gen.writeRawValue(producto);

        StdSerializer<Detalle> detalleStdSerializer = new DetalleJsonSerializer(Detalle.class, false, value);
        String detalle = JsonUtiles.getObjectMapper(Detalle.class, detalleStdSerializer, null).writeValueAsString(value.getDetalle());
        gen.writeFieldName("detalle");
        gen.writeRawValue(detalle);

        gen.writeNumberField("preset", value.getPreset());
        gen.writeStringField("fechaTurnoCarga", value.getFechaTurnoCarga().toString());
        gen.writeStringField("codigoExterno", value.getCodigoExterno());
        gen.writeNumberField("estado", value.getEstado());
        gen.writeNumberField("tara", value.getTara());
        gen.writeBooleanField("alarma", value.isAlarma());
        gen.writeNumberField("temperaturaUmbral", value.getTemperaturaUmbral());
        gen.writeNumberField("ultimaMasa", value.getUltimaMasa());
        gen.writeNumberField("ultimaDensidad", value.getUltimaDensidad());
        gen.writeNumberField("ultimaTemperatura", value.getUltimaTemperatura());
        gen.writeNumberField("ultimoCaudal", value.getUltimoCaudal());
        gen.writeNumberField("pesajeFinal", value.getPesajeFinal());
        gen.writeStringField("fechaDetalleFinal", value.getFechaDetalleFinal() != null ? value.getFechaDetalleFinal().toString() : "");
        gen.writeStringField("fechaDetalleInicial", value.getFechaDetalleInicial() != null ? value.getFechaDetalleInicial().toString() : "");

        double masa = value.getUltimaMasa();
        double caudal = value.getUltimoCaudal();
        double masaRestante = value.getPreset() - masa;
        double eta = caudal > 0 ? masaRestante / caudal : Double.POSITIVE_INFINITY;
        double horas = Math.floor(eta/3600);
        double minutos = Math.floor((eta%3600)/60);
        double segundos = Math.floor(eta%60);
        String tiempoEstimadoCarga = String.format("%02d:%02d:%02d", (int)horas, (int)minutos, (int)segundos);
        gen.writeStringField("tiempoEstimadoCarga", tiempoEstimadoCarga);
        //gen.writeNumberField("estado", value.getEstado());

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
/**
 const calculateTiempoTranscurrido = (carga) => {
    const fechaDetalleFinal = carga ? carga.fechaDetalle : actualOrder.fechaDetalleFinal

    const tiempo = (new Date(fechaDetalleFinal) - new Date(actualOrder.fechaDetalleInicial))
    const segundos = Math.floor((tiempo / 1000) % 60);
    const minutos = Math.floor((tiempo / 1000 / 60) % 60);
    const horas = Math.floor((tiempo / 1000 / 60 / 60) % 24);
    return${horas}h:${minutos}m:${segundos}s;
  }
* **/

/**
 {
 "numeroOrden": 1234567893,
 "camion": {
    "patente": "ABC623456",
 },
 "chofer": {
 "dni": 123457,
 },
 "cliente": {
 "razonSocial": "123746",
 },
 "producto": {
 "nombre": "coca",
 "descripcion": "comun",
 "code": "3ada1bfc-be15-4ad5-b0ac-67d15666d8ca"
 },
 "detalle": [
 {
 "masa": 500.0,
 "densidad": 0.9,
 "temperatura": 50.0,
 "caudal": 26.0,
 "fechaDetalle": "2024-02-20T01:32:40.109606-03:00",
 "tiempoEstimadoCarga": "00:00:40",
 "tiempoTranscurrido": "00:00:00"
 },
 {
 "masa": 700.0,
 "densidad": 0.9,
 "temperatura": 50.0,
 "caudal": 26.0,
 "fechaDetalle": "2024-02-20T01:32:55.079909-03:00",
 "tiempoEstimadoCarga": "00:00:32",
 "tiempoTranscurrido": "00:00:15"
 }
 ],
 "preset": 1540.0,
 "fechaTurnoCarga": "2022-12-20T17:03:54-03:00",
 "codigoExterno": "code123",
 "estado": 2,
 "tara": 25000,
 "alarma": true,
 "temperaturaUmbral": 40.0,
 "ultimaMasa": 700.0,
 "ultimaDensidad": 0.9,
 "ultimaTemperatura": 50.0,
 "ultimoCaudal": 26.0,
 "pesajeFinal": 0,
 "fechaDetalleFinal": "2024-02-20T01:32:55.079882-03:00",
 "fechaDetalleInicial": "2024-02-20T01:32:40.109536-03:00",
 "tiempoEstimadoCarga": "00:00:32",
 "tiempoTranscurrido": "00:00:15"
 }
 * **/