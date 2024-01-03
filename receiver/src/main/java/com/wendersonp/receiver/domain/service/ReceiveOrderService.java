package com.wendersonp.receiver.domain.service;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;

public interface ReceiveOrderService {

    OrderResponseStatusDTO validateAndSend(OrderDTO order);

    void validateOrder(OrderDTO order);
}
