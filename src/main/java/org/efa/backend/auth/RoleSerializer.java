package org.efa.backend.auth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class RoleSerializer extends StdSerializer<Role>  {
    public RoleSerializer(Class<Role> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(Role value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("role", value.getName());
        gen.writeEndObject();
    }
}
