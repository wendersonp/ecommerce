package com.wendersonp.processor.cucumber.configuration;

import com.wendersonp.processor.domain.repository.SaleOrderRepository;
import io.cucumber.java.After;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@CucumberContextConfiguration
@SpringBootTest
@DirtiesContext
@Import(TestChannelBinderConfiguration.class)
public class CucumberSpringConfiguration {

    @Autowired
    private SaleOrderRepository repository;


    @After
    void clearRepository() {
        repository.deleteAll();
    }
}
