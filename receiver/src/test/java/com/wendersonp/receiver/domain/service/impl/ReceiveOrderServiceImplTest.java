package com.wendersonp.receiver.domain.service.impl;

import com.wendersonp.receiver.domain.dto.ItemDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.dto.fixture.Fixture;
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
        order.setItens(new Fixture<ItemDTO>().listFromFile("static/items_valid.json", ItemDTO.class));
        order.setTotalItens(service.calculateTotal(order));

        doNothing().when(sendOrderService).sendToStream(argThat(orderInArgument -> {
            assertEquals(order, orderInArgument);
            return true;
        }));

        LocalDateTime now = LocalDateTime.now();
        var response = service.validateAndSend(order, now);
        var expected = new OrderResponseStatusDTO(RequestStatusEnum.EM_PROCESSAMENTO, now);

        assertEquals(response, expected);
    }

    @Test
    @DisplayName("must throw exception when total item value not valid")
    void validateAndSendTotalItensException() {
        OrderDTO order = factory.manufacturePojo(OrderDTO.class);
        order.setItens(new Fixture<ItemDTO>().listFromFile("static/items_valid.json", ItemDTO.class));

        LocalDateTime now = LocalDateTime.now();

        assertThrows(TotalValueNotValidException.class, () -> service.validateAndSend(order, now));
    }

}