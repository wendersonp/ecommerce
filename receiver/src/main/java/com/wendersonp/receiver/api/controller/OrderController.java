package com.wendersonp.receiver.api.controller;

import com.wendersonp.receiver.api.handler.ErrorDetail;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import com.wendersonp.receiver.domain.dto.OrderResponseStatusDTO;
import com.wendersonp.receiver.domain.service.ReceiveOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Tag(name = "VENDA", description = "Endpoints para processamento de ordens de venda")
public class OrderController {

    private final ReceiveOrderService orderService;

    @Operation(summary = "Submete uma ordem de venda para processamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Uma ordem de venda foi submetida para processamento com sucesso", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrderResponseStatusDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Um ou varios dos campos submetidos são invalidos", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDetail.class))
            })
    })
    @PostMapping("/autorizar-venda")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseStatusDTO authorizeOrder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Ordem de venda a ser processada")
            @Valid
            @RequestBody
            OrderDTO order,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @Parameter(
                    description = "Data para execução do processamento, no formato ISO 8601, corresponde a data atual por padrão",
                    example = "2022-05-11T15:22:33"
            )
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
            LocalDateTime date
    ) {
        return orderService.validateAndSend(order, date);
    }
}
