package cn.windery.sentinel.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> T parseObject(String source, Class<T> clazz) {
        try {
            return mapper.readValue(source, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toJsonString(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readTree(String source) {
        try {
            return mapper.readTree(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
