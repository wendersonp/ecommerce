package com.wendersonp.receiver.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {
    @Digits(integer = 20, fraction = 0)
    private String numeroPedido;

    @NotEmpty
    private String numeroOrdemExterno;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataAutorizacao;
}
