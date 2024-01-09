package com.wendersonp.receiver.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

import static com.wendersonp.receiver.domain.util.ValidationMessages.*;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    @NotBlank(message = NOT_BLANK)
    private String canal;

    @NotBlank(message = NOT_BLANK)
    @Digits(integer = 5, fraction = 0)
    private String empresa;

    @NotBlank(message = NOT_BLANK)
    @Digits(integer = 4, fraction = 0)
    private String loja;

    @Positive
    private Integer pdv;

    @NotNull(message = NOT_NULL)
    @Valid
    private OrderInfoDTO ordemPedido;

    @Valid
    @NotNull(message = NOT_NULL)
    private ClientDTO cliente;

    @NotNull(message = NOT_NULL)
    @Positive
    private BigInteger totalItens;

    @NotNull(message = NOT_NULL)
    @Positive
    private BigInteger quantidadeItens;

    @NotEmpty(message = NOT_EMPTY)
    @Valid
    private List<ItemDTO> itens;
}
