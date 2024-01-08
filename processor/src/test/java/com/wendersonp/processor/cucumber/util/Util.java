package com.wendersonp.processor.cucumber.util;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Util {

    public static final DateTimeFormatter formatterWithoutMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static final DateTimeFormatter formatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static <T, R> R castIfNotNull(T object, Function<T, R> cast) {
        return object != null ? cast.apply(object) : null;
    }

    public static String toCamelCase(String value) {
        var valueAsCharArray = value.replace(" ", "").toCharArray();
        valueAsCharArray[0] = Character.toLowerCase(valueAsCharArray[0]);

        return new String(valueAsCharArray);
    }

    public static Map<String, String> formatMapToResponseBody(Map<String, String> data) {
        Map<String, String> responseMap = new HashMap<>();

        data.forEach((key, value) ->
            responseMap.put(toCamelCase(key), value)
        );

        return responseMap;
    }

    public static Map<String, Object> formatKeysToCamelCase(Map<String, Object> data) {
        Map<String, Object> responseMap = new HashMap<>();

        data.forEach((key, value) ->
            responseMap.put(toCamelCase(key), value)
        );

        return responseMap;
    }
}
