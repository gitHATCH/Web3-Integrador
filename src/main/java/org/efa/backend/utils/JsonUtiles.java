package org.efa.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class JsonUtiles {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static ObjectMapper getObjectMapper(Class clazz, StdSerializer ser, String dateFormat) {
        ObjectMapper mapper = new ObjectMapper();
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

    public static double getDouble(JsonNode node, String[] attrs, double defaultValue) {
        Double r = null;
        for (String attr : attrs) {
            if (node.get(attr) != null && node.get(attr).isDouble()) {
                r = node.get(attr).asDouble();
                break;
            }
        }
        if (r == null)
            r = defaultValue;
        return r;
    }

    public static float getFloat(JsonNode node, String[] attrs, float defaultValue) {
        Float r = null;
        for (String attr : attrs) {
            if (node.get(attr) != null && node.get(attr).isFloat()) {
                r = node.get(attr).floatValue();
                break;
            }
        }
        if (r == null)
            r = defaultValue;
        return r;
    }

    public static int getInteger(JsonNode node, String[] attrs, int defaultValue) {
        Integer r = null;
        for (String attr : attrs) {
            if (node.get(attr) != null && node.get(attr).isInt()) {
                r = node.get(attr).asInt();
                break;
            }
        }
        if (r == null)
            r = defaultValue;
        return r;
    }

    public static long getLong(JsonNode node, String[] attrs, long defaultValue) {
        Long r = null;
        for (String attr : attrs) {
            if (node.get(attr) != null && node.get(attr).isLong()) {
                r = node.get(attr).asLong();
                break;
            }
        }
        if (r == null)
            r = defaultValue;
        return r;
    }

    public static boolean getBoolean(JsonNode node, String[] attrs, boolean defaultValue) {
        Boolean r = null;
        for (String attr : attrs) {
            if (node.get(attr) != null && node.get(attr).isBoolean()) {
                r = node.get(attr).asBoolean();
                break;
            }
        }
        if (r == null)
            r = defaultValue;
        return r;
    }
}
