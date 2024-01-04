package com.wendersonp.receiver.domain.service.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.enumeration.RequestStatusEnum;
import com.wendersonp.receiver.domain.exceptions.TotalValueNotValidException;
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
        validateOrder(order);
        sender.sendToStream(order);
        return new OrderResponseStatusDTO(RequestStatusEnum.EM_PROCESSAMENTO, LocalDateTime.now());
    }

    @Override
    public void validateOrder(OrderDTO order) {
        if (order.getTotalItens() != calculateTotal(order)) {
            throw new TotalValueNotValidException();
        }
    }

    public int calculateTotal(OrderDTO orderDTO) {
        return orderDTO.getItens()
                .stream()
                .reduce(0,
                        (subtotal, item) -> subtotal + item.getQuantidade() * item.getValor(),
                        Integer::sum
                );
    }
}
