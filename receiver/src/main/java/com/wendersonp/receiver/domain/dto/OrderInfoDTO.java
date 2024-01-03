package com.wendersonp.receiver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderInfoDTO {
    private String numeroPedido;
    private String numeroOrdemExterno;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataAutorizacao;
}
