package com.wendersonp.receiver.domain.service.impl;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.enumeration.RequestStatusEnum;
import com.wendersonp.receiver.domain.exceptions.TotalValueNotValidException;
import com.wendersonp.receiver.domain.service.ReceiveOrderService;
import com.wendersonp.receiver.domain.service.SendOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReceiveOrderServiceImpl implements ReceiveOrderService {

    private final SendOrderService sender;
    @Override
    public OrderResponseStatusDTO validateAndSend(OrderDTO order, LocalDateTime date) {
        validateOrder(order);
        sender.sendToStream(order);
        return new OrderResponseStatusDTO(RequestStatusEnum.EM_PROCESSAMENTO, date);
    }

    @Override
    public void validateOrder(OrderDTO order) {
        if (!order.getTotalItens().equals(calculateTotal(order))) {
            throw new TotalValueNotValidException();
        }
    }

    public BigInteger calculateTotal(OrderDTO orderDTO) {
        return orderDTO.getItens()
                .stream()
                .reduce(BigInteger.ZERO,
                        (subtotal, item) ->
                                BigInteger
                                        .valueOf(item.getValor())
                                        .multiply(BigInteger.valueOf(item.getQuantidade()))
                                        .add(subtotal),
                        BigInteger::add
                );
    }
}
