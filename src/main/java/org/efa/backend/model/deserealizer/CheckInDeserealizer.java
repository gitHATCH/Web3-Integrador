package org.efa.backend.model.deserealizer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.efa.backend.model.Orden;
import org.efa.backend.utils.JsonUtiles;

import java.io.IOException;

public class CheckInDeserealizer extends StdDeserializer<Orden> {
    public CheckInDeserealizer(Class<?> src) {
        super(src);
    }


    @Override
    public Orden deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        Orden r = new Orden();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String tara = JsonUtiles.getString(node, "tara,pesaje_inicial".split(","),"0");
        r.setTara(Long.parseLong(tara));
        return r;
    }
}
