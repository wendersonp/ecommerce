package com.wendersonp.receiver.cucumber.util;

import java.util.function.Function;

public class Util {

    public static <T, R> R castIfNotNull(T object, Function<T, R> cast) {
        return object != null ? cast.apply(object) : null;
    }
}
