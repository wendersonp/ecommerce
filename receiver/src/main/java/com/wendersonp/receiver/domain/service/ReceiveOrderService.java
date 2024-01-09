package com.wendersonp.receiver.domain.service;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;

import java.time.LocalDateTime;

public interface ReceiveOrderService {

    OrderResponseStatusDTO validateAndSend(OrderDTO order, LocalDateTime date);

    void validateOrder(OrderDTO order);
}
