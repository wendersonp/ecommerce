package com.wendersonp.processor.cucumber.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = {"com.wendersonp.processor.cucumber.configuration",
                "com.wendersonp.processor.cucumber.steps"},
        plugin = {"pretty",
                  "html:target/cucumber-html-report.html",
                  "json:target/cucumber.json",
                  "rerun:target/cucumber-api-rerun.txt"
        }
)
public class CucumberIntegrationTest {
}
