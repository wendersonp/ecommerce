package com.wendersonp.receiver.cucumber.data;

import com.wendersonp.receiver.cucumber.util.LocalDateTimeWrapper;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import io.cucumber.spring.CucumberTestContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.ApplicationScope;


@Configuration
public class DataInitializer {

    @Bean
    @Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
    public OrderDTO orderDTOData() {
        return new OrderDTO();
    }

    @Bean
    @ApplicationScope
    public LocalDateTimeWrapper dateTimeData() {
        return new LocalDateTimeWrapper();
    }

}
