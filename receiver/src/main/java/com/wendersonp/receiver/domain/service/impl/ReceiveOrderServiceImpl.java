package com.wendersonp.receiver.domain.service.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.enumeration.RequestStatusEnum;
import com.wendersonp.receiver.domain.service.ReceiveOrderService;
import com.wendersonp.receiver.domain.service.SendOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReceiveOrderServiceImpl implements ReceiveOrderService {

    private final SendOrderService sender;
    @Override
    public OrderResponseStatusDTO validateAndSend(OrderDTO order) {
        sender.sendToStream(order);
        return new OrderResponseStatusDTO(RequestStatusEnum.EM_PROCESSAMENTO, LocalDateTime.now());
    }
}
