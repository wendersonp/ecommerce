package com.wendersonp.receiver.cucumber.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/",
        glue = {"com.wendersonp.receiver.cucumber.configuration",
                "com.wendersonp.receiver.cucumber.steps"},
        dryRun = true,
        plugin = {"pretty",
                  "html:target/cucumber-html-report.html",
                  "json:target/cucumber.json",
                  "rerun:target/cucumber-api-rerun.txt"
        }
)
public class CucumberIntegrationTest {
}
