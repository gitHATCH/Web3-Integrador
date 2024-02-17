package org.efa.backend.auth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class RoleJsonSerializer extends StdSerializer<Role> {
 public RoleJsonSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }
  @Override
    public void serialize(Role role, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

            jsonGenerator.writeString(role.getName());

}
}