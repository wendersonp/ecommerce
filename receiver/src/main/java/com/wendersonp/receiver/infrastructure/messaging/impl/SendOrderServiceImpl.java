package com.wendersonp.receiver.infrastructure.messaging.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.service.SendOrderService;
import com.wendersonp.receiver.infrastructure.messaging.OrderChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendOrderServiceImpl implements SendOrderService {

    private final OrderChannelService orderChannel;
    @Override
    public void sendToStream(OrderDTO order) {
        orderChannel.sendOrder(order);
    }

}
