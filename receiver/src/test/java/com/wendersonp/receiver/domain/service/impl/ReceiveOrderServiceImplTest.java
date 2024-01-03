package com.wendersonp.receiver.domain.service.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.enumeration.RequestStatusEnum;
import com.wendersonp.receiver.domain.exceptions.TotalValueNotValidException;
import com.wendersonp.receiver.domain.service.SendOrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReceiveOrderServiceImplTest {

    private final PodamFactory factory = new PodamFactoryImpl();

    @Mock
    private SendOrderService sendOrderService;

    @InjectMocks
    private ReceiveOrderServiceImpl service;

    @Test
    @DisplayName("send order to messaging and return with success")
    void validateAndSend() {
        OrderDTO order = factory.manufacturePojo(OrderDTO.class);
        order.setTotalItens(service.calculateTotal(order));
        doNothing().when(sendOrderService).sendToStream(argThat(orderInArgument -> {
            assertEquals(order, orderInArgument);
            return true;
        }));
        var response = service.validateAndSend(order);
        assertEquals(
                new OrderResponseStatusDTO(RequestStatusEnum.EM_PROCESSAMENTO, LocalDateTime.now()),
                response
        );
    }

    @Test
    @DisplayName("must throw exception when total item value not valid")
    void validateAndSendTotalItensException() {
        OrderDTO order = factory.manufacturePojo(OrderDTO.class);
        assertThrows(TotalValueNotValidException.class, () -> service.validateAndSend(order));
    }
}