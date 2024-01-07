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

import java.util.Arrays;

@RequiredArgsConstructor
public class FailedSaleOrderFactoryImpl implements SaleOrderEntityFactory {

    private final OrderDTO order;

    private final RuntimeException exception;

    private static final int MOTIVO_LIMIT = 255;
    private static final String RESPONSE_BODY = "response-body: ";

    @Override
    public SaleOrderEntity create() {
        return SaleOrderEntity.builder()
                .canal(order.getCanal())
                .codigoEmpresa(Integer.parseInt(order.getEmpresa()))
                .codigoLoja(Integer.parseInt(order.getLoja()))
                .numeroPdv(order.getPdv())
                .numeroPedido(order.getOrdemPedido().getNumeroPedido())
                .numeroOrdemExterno(order.getOrdemPedido().getNumeroOrdemExterno())
                .valorTotal(Util.divideByHundred(order.getTotalItens()))
                .qtdItem(order.getQuantidadeItens())
                .vendaRequest(Util.parseToJson(order))
                .dataRequisicao(order.getOrdemPedido().getDataAutorizacao())
                .situacao(OrderStatusEnum.ERRO)
                .motivo(buildMotivo())
                .build();
    }

    private String buildMotivo() {
        StringBuilder stringBuilder = new StringBuilder();

        if (exception instanceof FeeProcessingException feeProcessingException){
            stringBuilder.append("error: FeeProcessingException; ");
            stringBuilder.append(RESPONSE_BODY).append(feeProcessingException.getResponseBody()).append("; ");
        } else if (exception instanceof InvoiceProcessingException invoiceProcessingException) {
            stringBuilder.append("error: InvoiceProcessingException; ");
            stringBuilder.append(RESPONSE_BODY).append(invoiceProcessingException.getResponseBody()).append("; ");
        } else if (exception instanceof ChannelCallbackException channelCallbackException) {
            stringBuilder.append("error: ChannelCallbackException; ");
            stringBuilder.append(RESPONSE_BODY).append(channelCallbackException.getResponseBody()).append("; ");
        } else {
            stringBuilder.append("error: ")
                    .append(exception.getClass().getSimpleName()).append("; ");
        }

        stringBuilder.append("message: ")
                .append(exception.getMessage()).append("; ");

        stringBuilder.append("stack-trace: ")
                .append(Arrays.toString(exception.getStackTrace())).append(";");

        var reason = stringBuilder.toString();
        return trimStringToLimit(reason);
    }

    private String trimStringToLimit(String string) {
        return string.length() < MOTIVO_LIMIT ? string : string.substring(0, MOTIVO_LIMIT);
    }
}
