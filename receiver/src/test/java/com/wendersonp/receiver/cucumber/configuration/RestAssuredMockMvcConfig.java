package com.wendersonp.receiver.cucumber.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.path.json.mapper.factory.Jackson2ObjectMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;


public class RestAssuredMockMvcConfig {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void initializeRestAssuredMockMvcWebApplicationContext() {
        Jackson2ObjectMapperFactory objectConfig = (clazz, string) -> objectMapper;
        RestAssuredMockMvc.config()
                .objectMapperConfig(
                        ObjectMapperConfig
                        .objectMapperConfig()
                        .jackson2ObjectMapperFactory(objectConfig)
                );
        RestAssuredMockMvc.webAppContextSetup(context);
    }
}
