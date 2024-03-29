package com.wendersonp.receiver.domain.dto.fixture;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.util.List;

public class Fixture<T> {

    private final ObjectMapper objectMapper;

    public Fixture() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @SneakyThrows
    public T fromFile(String fileName, Class<T> type) {
        try (InputStream content = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return objectMapper.readValue(content, type);
        }
    }

    @SneakyThrows
    public List<T> listFromFile(String fileName, Class<T> type) {
        try (InputStream content = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return objectMapper.readValue(
                    content,
                    objectMapper
                            .getTypeFactory()
                            .constructCollectionType(List.class, type)
            );
        }
    }
}
