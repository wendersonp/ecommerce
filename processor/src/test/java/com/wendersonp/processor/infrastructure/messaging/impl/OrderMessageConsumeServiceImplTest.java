package com.wendersonp.processor.infrastructure.messaging.impl;

import com.wendersonp.processor.domain.dto.fixture.Fixture;
import com.wendersonp.processor.domain.service.OrderProcessorService;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderMessageConsumeServiceImplTest {

    @Mock
    private OrderProcessorService processorService;

    @InjectMocks
    private OrderMessageConsumeServiceImpl service;

    @Test
    @DisplayName("must receive and send order to process service with success")
    void consumeOrderTest() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);
        service.consumeOrder(order);
        verify(processorService, times(1)).process(order);
    }

    @Test
    @DisplayName("must return order consumer and call service")
    void orderMessageConsumerTest() {
        OrderDTO order = new Fixture<OrderDTO>()
                .fromFile("static/order_valid.json", OrderDTO.class);
        var consumer = service.orderMessageConsumer();
        consumer.accept(order);
        verify(processorService, times(1)).process(order);
    }
}