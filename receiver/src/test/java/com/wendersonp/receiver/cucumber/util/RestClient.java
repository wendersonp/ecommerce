package com.wendersonp.receiver.cucumber.util;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class RestClient {

    public static MockMvcRequestSpecification getRequestSpecification() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .log()
                .everything();
    }
}
