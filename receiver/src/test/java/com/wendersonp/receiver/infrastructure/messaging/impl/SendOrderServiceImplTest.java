package com.wendersonp.receiver.infrastructure.messaging.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.infrastructure.messaging.OrderChannelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class SendOrderServiceImplTest {

    private final PodamFactory factory = new PodamFactoryImpl();

    @Mock
    private OrderChannelService orderChannel;

    @InjectMocks
    private SendOrderServiceImpl service;

    @Test
    @DisplayName("Must send order to channel service with success")
    void sendToStream() {
        var order = factory.manufacturePojo(OrderDTO.class);
        doNothing().when(orderChannel).sendOrder(argThat(
                orderInArgument -> {
                    assertEquals(order, orderInArgument);
                    return true;
                }));
        service.sendToStream(order);
    }
}