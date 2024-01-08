package com.wendersonp.processor.cucumber.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.wendersonp.processor.cucumber.data.StringToIntDeserializer;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class ObjectMapperConfig {
    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setupMapper() {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.enable(DeserializationFeature.ACCEPT_FLOAT_AS_INT);
        SimpleModule stringToIntDeserializerModule = new SimpleModule();
        stringToIntDeserializerModule.addDeserializer(Integer.class, new StringToIntDeserializer());
        mapper.registerModule(stringToIntDeserializerModule);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
