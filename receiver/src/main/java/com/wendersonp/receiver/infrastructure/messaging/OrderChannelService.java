package com.wendersonp.receiver.infrastructure.messaging;

import com.wendersonp.receiver.domain.dto.OrderDTO;

public interface OrderChannelService {

    void sendOrder(OrderDTO order);
}
