package com.wendersonp.processor.domain.factory.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.factory.SaleOrderEntityFactory;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.util.Util;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
public class SuccessfulSaleOrderFactoryImpl implements SaleOrderEntityFactory {

    private final OrderDTO order;

    private final InvoiceDTO invoiceDTO;
    @Override
    public SaleOrderEntity create() {
        return SaleOrderEntity.builder()
                .canal(order.getCanal())
                .codigoEmpresa(Integer.parseInt(order.getEmpresa()))
                .codigoLoja(Integer.parseInt(order.getLoja()))
                .numeroPdv(order.getPdv())
                .numeroPedido(invoiceDTO.getNumeroPedido())
                .numeroOrdemExterno(invoiceDTO.getNumeroOrdemExterno())
                .valorTotal(Util.parseAndDivideByHundred(order.getTotalItens()))
                .qtdItem(BigInteger.valueOf(order.getQuantidadeItens()))
                .vendaRequest(Util.parseToJson(order))
                .dataRequisicao(order.getOrdemPedido().getDataAutorizacao())
                .chaveNfe(invoiceDTO.getChaveNFE())
                .numeroNota(invoiceDTO.getNumeroNota())
                .dataEmissao(invoiceDTO.getDataEmissao())
                .pdf(invoiceDTO.getPdf())
                .situacao(invoiceDTO.getStatus())
                .build();
    }


}
