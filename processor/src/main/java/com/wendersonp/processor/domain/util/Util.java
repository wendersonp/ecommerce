package com.wendersonp.processor.domain.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Util {

    private Util(){}

    private static final ObjectMapper mapper = createMapper();

    public static BigDecimal divideByHundred(BigInteger totalItens) {
        return new BigDecimal(totalItens)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
    }

    @SneakyThrows
    public static String parseToJson(Object object) {
        return mapper.writeValueAsString(object);
    }

    private static ObjectMapper createMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
