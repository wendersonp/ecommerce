package com.wendersonp.processor.domain.factory.impl;

import com.wendersonp.processor.domain.enumeration.OrderStatusEnum;
import com.wendersonp.processor.domain.exception.ChannelCallbackException;
import com.wendersonp.processor.domain.exception.FeeProcessingException;
import com.wendersonp.processor.domain.exception.InvoiceProcessingException;
import com.wendersonp.processor.domain.factory.SaleOrderEntityFactory;
import com.wendersonp.processor.domain.models.SaleOrderEntity;
import com.wendersonp.processor.domain.util.Util;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Arrays;

@RequiredArgsConstructor
public class FailedSaleOrderFactoryImpl implements SaleOrderEntityFactory {

    private final OrderDTO order;

    private final RuntimeException exception;

    private static final int MOTIVO_LIMIT = 255;

    @Override
    public SaleOrderEntity create() {
        return SaleOrderEntity.builder()
                .canal(order.getCanal())
                .codigoEmpresa(Integer.parseInt(order.getEmpresa()))
                .codigoLoja(Integer.parseInt(order.getLoja()))
                .numeroPdv(order.getPdv())
                .numeroPedido(order.getOrdemPedido().getNumeroPedido())
                .numeroOrdemExterno(order.getOrdemPedido().getNumeroOrdemExterno())
                .valorTotal(Util.parseAndDivideByHundred(order.getTotalItens()))
                .qtdItem(BigInteger.valueOf(order.getQuantidadeItens()))
                .vendaRequest(Util.parseToJson(order))
                .dataRequisicao(order.getOrdemPedido().getDataAutorizacao())
                .situacao(OrderStatusEnum.ERRO)
                .motivo(buildMotivo())
                .build();
    }

    private String buildMotivo() {
        StringBuilder stringBuilder = new StringBuilder();

        if (exception instanceof FeeProcessingException){
            stringBuilder.append("error: FeeProcessingException; ");
        } else if (exception instanceof InvoiceProcessingException) {
            stringBuilder.append("error: InvoiceProcessingException; ");
        } else if (exception instanceof ChannelCallbackException) {
            stringBuilder.append("error: ChannelCallbackException; ");
        } else {
            stringBuilder.append("error: ")
                    .append(exception.getCause().getClass().getSimpleName());
        }

        stringBuilder.append("cause: ")
                .append(exception.getCause().getMessage()).append("; ");

        stringBuilder.append("stackTrace: ")
                .append(Arrays.toString(exception.getStackTrace())).append(";");

        return stringBuilder.substring(0, MOTIVO_LIMIT);
    }
}
