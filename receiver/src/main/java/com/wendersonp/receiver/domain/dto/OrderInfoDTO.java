package com.wendersonp.receiver.domain.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.wendersonp.receiver.domain.util.ValidationMessages.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {
    @NotBlank(message = NOT_BLANK)
    @Digits(integer = 20, fraction = 0)
    private String numeroPedido;

    @NotBlank(message = NOT_BLANK)
    @Pattern(regexp = "(\\d){12,}-\\d",message = ORDER_EXTERNAL_FORMAT)
    private String numeroOrdemExterno;

    @NotNull(message = NOT_NULL)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataAutorizacao;
}
