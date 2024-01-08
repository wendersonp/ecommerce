package com.wendersonp.processor.infrastructure.messaging.impl;

import com.wendersonp.processor.domain.service.OrderProcessorService;
import com.wendersonp.processor.infrastructure.messaging.OrderMessageConsumeService;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class OrderMessageConsumeServiceImpl implements OrderMessageConsumeService {

    private final OrderProcessorService orderProcessorService;
    @Override
    public void consumeOrder(OrderDTO order) {
        orderProcessorService.process(order);
    }

    @Bean
    public Consumer<OrderDTO> autorizarVendaQueue() {
        return this::consumeOrder;
    }
}
