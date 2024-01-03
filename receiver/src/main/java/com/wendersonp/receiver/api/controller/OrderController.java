package com.wendersonp.receiver.api.controller;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.service.ReceiveOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final ReceiveOrderService orderService;

    @PostMapping("/autorizar-venda")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseStatusDTO authorizeOrder(@Valid @RequestBody OrderDTO order) {
        return orderService.validateAndSend(order);
    }
}
