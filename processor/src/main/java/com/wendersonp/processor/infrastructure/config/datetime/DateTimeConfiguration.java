package com.wendersonp.processor.infrastructure.config.datetime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Clock;

@Profile("!test")
@Configuration
public class DateTimeConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
