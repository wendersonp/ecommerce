package com.wendersonp.receiver.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {
    @NotEmpty
    private String canal;

    @Digits(integer = 5, fraction = 0)
    private String empresa;

    @Digits(integer = 4, fraction = 0)
    private String loja;

    @Positive
    private int pdv;

    @Valid
    private OrderInfoDTO ordemPedido;

    @Valid
    private ClientDTO cliente;

    @Positive
    private int totalItens;

    @Positive
    private int quantidadeItens;

    @Valid
    private List<ItemDTO> itens;
}
