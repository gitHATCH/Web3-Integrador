package org.efa.backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.model.Cliente;

import java.io.IOException;

public class ClienteJsonSerializer extends StdSerializer<Cliente> {

    private static final long serialVersionUID = -4079218519254704976L;

    public ClienteJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Cliente cliente, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("razonSocial", cliente.getRazonSocial());
        jsonGenerator.writeNumberField("contacto", cliente.getContacto());
        jsonGenerator.writeStringField("code", cliente.getCode());
        jsonGenerator.writeEndObject();
    }
}
