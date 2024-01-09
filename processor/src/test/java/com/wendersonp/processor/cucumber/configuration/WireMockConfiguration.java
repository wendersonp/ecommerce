package com.wendersonp.processor.cucumber.configuration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class WireMockConfiguration {

    private WireMockServer server;
    @Before
    public void setUp() {
        server = new WireMockServer(options().port(9001));
        server.start();
        WireMock.configureFor("localhost", 9001);
    }

    @After
    public void destroy() {
        server.stop();
    }
}
