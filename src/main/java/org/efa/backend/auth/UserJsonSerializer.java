package org.efa.backend.auth;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;
public class UserJsonSerializer  extends StdSerializer<User> {

    private String token;
    private static final long serialVersionUID = -3706327488880784297L;

    public UserJsonSerializer(Class<?> t, boolean dummy,String token) {
        super(t, dummy);
        this.token=token;
    }


    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("username", value.getUsername());
        String componentsStr = JsonUtiles
                .getObjectMapper(Role.class, new RoleJsonSerializer(Role.class, false), null)
                .writeValueAsString(value.getRoles());
        gen.writeFieldName("roles");
        gen.writeRawValue(componentsStr);
        gen.writeStringField("email", value.getEmail());
        gen.writeStringField("authtoken", this.token);

        gen.writeEndObject();

    }
}

