package com.wendersonp.processor.cucumber.steps;

import com.wendersonp.processor.domain.repository.SaleOrderRepository;
import io.cucumber.java.After;
import org.springframework.beans.factory.annotation.Autowired;

public class AfterScenarioSteps {

    @Autowired
    private SaleOrderRepository repository;

    @After
    public void clearRepository() {
        repository.deleteAll();
    }
}
