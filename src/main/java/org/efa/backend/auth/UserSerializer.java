package org.efa.backend.auth;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.efa.backend.auth.User;
import org.efa.backend.miscellaneous.Paths;
import org.efa.backend.utils.JsonUtiles;

public class UserSerializer extends StdSerializer<User>{
    public UserSerializer(Class<User> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        gen.writeStringField("User", value.getUsername());


        String componentsStr = JsonUtiles
                .getObjectMapper(Role.class, new RoleSerializer(Role.class, false), null)
                .writeValueAsString(value.getRoles());
        gen.writeFieldName("roles");
        gen.writeRawValue(componentsStr);


        gen.writeStringField("EMail", value.getEmail());


        String token = JWT.create().withSubject(value.getUsername())
                .withClaim("internalId", value.getIdUser())
                .withClaim("roles", new ArrayList<String>(value.getAuthoritiesStr()))
                .withClaim("email", value.getEmail())
                .withClaim("version", "1.0.0")
                .withExpiresAt(new Date(System.currentTimeMillis() + Paths.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(Paths.SECRET.getBytes()));


        gen.writeStringField("Authtoken", token);
        gen.writeEndObject();

    }

}
