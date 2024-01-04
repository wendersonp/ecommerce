package com.wendersonp.processor.infrastructure.config.feign;

import feign.Retryer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class FeignConfiguration {

    //TODO: Se possível, adicionar e configurar o Hystrix para CircuitBreaker
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(300L, TimeUnit.SECONDS.toMillis(5L), 8);
    }
}
