package com.wendersonp.processor.cucumber.data;

import com.wendersonp.processor.cucumber.util.LocalDateTimeWrapper;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.infrastructure.client.channel.CallbackChannelClient;
import com.wendersonp.processor.infrastructure.client.sefaz.SefazClient;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import io.cucumber.spring.CucumberTestContext;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.ApplicationScope;

import java.time.Clock;


@Configuration
public class DataInitializer {

    @Bean
    @Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
    public SaleOrderEntity saleOrderEntity() { return new SaleOrderEntity(); }

    @Bean
    @Scope(CucumberTestContext.SCOPE_CUCUMBER_GLUE)
    public OrderDTO orderDTOData() {
        return new OrderDTO();
    }

    @Bean
    @Primary
    public SefazClient sefazClient(SefazClient client) {
        return Mockito.spy(client);
    }

    @Bean
    @Primary
    public CallbackChannelClient callbackChannelClient (CallbackChannelClient client) {
        return Mockito.spy(client);
    }

    @Bean
    @ApplicationScope
    public LocalDateTimeWrapper dateTimeData() {
        return new LocalDateTimeWrapper();
    }

    @Profile("test")
    @Bean
    public Clock clock() {
        return Mockito.mock(Clock.class);
    }

}
