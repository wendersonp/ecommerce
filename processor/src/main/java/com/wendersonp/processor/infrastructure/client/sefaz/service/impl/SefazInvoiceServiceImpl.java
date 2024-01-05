package com.wendersonp.processor.infrastructure.client.sefaz.service.impl;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.exception.InvoiceProcessingException;
import com.wendersonp.processor.domain.service.InvoiceService;
import com.wendersonp.processor.infrastructure.client.sefaz.SefazClient;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazRequestOrderDTO;
import com.wendersonp.receiver.domain.dto.OrderDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SefazInvoiceServiceImpl implements InvoiceService {

    private final SefazClient sefazClient;

    @Override
    public InvoiceDTO authorizeOrder(OrderDTO order, List<ItemFeesDTO> itemFees) {
        var request = new SefazRequestOrderDTO(order, itemFees);
        try {
            var response = sefazClient.authorizeOrder(request);
            return response.toInvoiceDTO(order);
        } catch (FeignException exception) {
            log.error("Erro processando ordem no SEFAZ", exception);
            throw new InvoiceProcessingException(
                    exception.getMessage(),
                    exception.getCause(),
                    exception.contentUTF8()
            );
        }
    }
}
