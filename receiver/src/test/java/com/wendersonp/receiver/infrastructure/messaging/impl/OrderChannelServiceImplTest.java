package com.wendersonp.receiver.infrastructure.messaging.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class OrderChannelServiceImplTest {

    private final PodamFactory factory = new PodamFactoryImpl()
;
    @Spy
    private Queue<OrderDTO> orderQueue = new LinkedBlockingQueue<>();

    @InjectMocks
    private OrderChannelServiceImpl service;

    @Test
    @DisplayName("Must add order to queue with success")
    void sendOrder() {
        var order = factory.manufacturePojo(OrderDTO.class);
        service.sendOrder(order);
        verify(orderQueue, times(1)).offer(any());
        Supplier<OrderDTO> supplier = service.autorizarVendaQueue();
        assertEquals(supplier.get(), order);
    }
}