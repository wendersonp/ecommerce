package com.wendersonp.receiver.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderDTO {
    private String canal;
    private String empresa;
    private String loja;
    private int pdv;
    private OrderInfoDTO ordemPedido;
    private ClientDTO cliente;
    private int totalItens;
    private int quantidadeItens;
    private List<ItemDTO> itens;
}
