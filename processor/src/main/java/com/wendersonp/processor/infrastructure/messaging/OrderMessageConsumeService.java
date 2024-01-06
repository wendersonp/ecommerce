package com.wendersonp.processor.infrastructure.messaging;

import com.wendersonp.receiver.domain.dto.OrderDTO;

public interface OrderMessageConsumeService {

    void consumeOrder(OrderDTO order);
}
