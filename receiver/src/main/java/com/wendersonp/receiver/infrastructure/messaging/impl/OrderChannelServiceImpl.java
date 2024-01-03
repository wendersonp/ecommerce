package com.wendersonp.receiver.infrastructure.messaging.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.infrastructure.messaging.OrderChannelService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

@Service
public class OrderChannelServiceImpl implements OrderChannelService {

    private final Queue<OrderDTO> orderQueue = new LinkedBlockingQueue<>();

    @Bean
    public Supplier<OrderDTO> orderMessageSupplier() {
        return orderQueue::poll;
    }

    @Override
    public void sendOrder(OrderDTO order) {
        orderQueue.offer(order);
    }
}
