package com.wendersonp.receiver.api.controller;

import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.service.ReceiveOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final ReceiveOrderService orderService;

    @PostMapping("/autorizar-venda")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseStatusDTO authorizeOrder(
            @Valid @RequestBody OrderDTO order,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}") LocalDateTime date) {
        return orderService.validateAndSend(order, date);
    }
}
