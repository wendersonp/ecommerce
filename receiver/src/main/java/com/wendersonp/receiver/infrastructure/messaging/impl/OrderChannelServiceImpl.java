package com.wendersonp.receiver.infrastructure.messaging.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.infrastructure.messaging.OrderChannelService;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

@Service
@NoArgsConstructor
public class OrderChannelServiceImpl implements OrderChannelService {

    private Queue<OrderDTO> orderQueue = new LinkedBlockingQueue<>();

    public OrderChannelServiceImpl(Queue<OrderDTO> queue) {
        orderQueue = queue;
    }

    @Bean
    public Supplier<OrderDTO> autorizarVendaQueue() {
        return orderQueue::poll;
    }

    @Override
    public void sendOrder(OrderDTO order) {
        orderQueue.offer(order);
    }
}
