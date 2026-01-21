package tech.pmman.util;

import tools.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Json serialize failed", e);
        }
    }

    public static <T> T fromJson(Class<T> type, String rawJson) {
        try {
            return OBJECT_MAPPER.readValue(rawJson, type);
        } catch (Exception e) {
            throw new RuntimeException("Json deserialize failed", e);
        }
    }
}
