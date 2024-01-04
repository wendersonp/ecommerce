package com.wendersonp.processor.domain.service;

import com.wendersonp.receiver.domain.dto.OrderDTO;

public interface OrderProcessorService {

    void process(OrderDTO order);
}
