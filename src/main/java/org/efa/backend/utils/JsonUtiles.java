package org.efa.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;
import java.util.Locale;

public final class JsonUtiles {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ObjectMapper getObjectMapper(Class clazz, StdSerializer ser, String dateFormat) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String defaultFormat = "yyyy-MM-dd'T'HH:mm:ssZ";
        if (dateFormat != null)
            defaultFormat = dateFormat;
        SimpleDateFormat df = new SimpleDateFormat(defaultFormat, Locale.getDefault());
        SimpleModule module = new SimpleModule();
        if (ser != null) {
            module.addSerializer(clazz, ser);
        }
        mapper.setDateFormat(df);
        mapper.registerModule(module);
        return mapper;

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ObjectMapper getObjectMapper(Class clazz, StdDeserializer deser) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        if (deser != null) {
            module.addDeserializer(clazz, deser);
        }
        mapper.registerModule(module);
        return mapper;

    }

    public static String getString(JsonNode node, String[] attrs, String defaultValue) {
        String r = null;
        for (String attr : attrs) {
            if (node.get(attr) != null) {
                r = node.get(attr).asText();
                break;
            }
        }
        if (r == null)
            r = defaultValue;
        return r;
    }

}
