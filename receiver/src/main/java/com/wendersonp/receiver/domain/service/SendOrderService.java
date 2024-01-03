package com.wendersonp.receiver.domain.service;

import com.wendersonp.receiver.domain.dto.OrderDTO;

public interface SendOrderService {

    void sendToStream(OrderDTO order);
}
