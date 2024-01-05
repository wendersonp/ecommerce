package com.wendersonp.processor.infrastructure.client.tribute.service.impl;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.exception.FeeProcessingException;
import com.wendersonp.processor.infrastructure.client.tribute.TributeItemClient;
import com.wendersonp.processor.infrastructure.client.tribute.service.TributeItemConnector;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TributeItemConnectorImpl implements TributeItemConnector {

    private final TributeItemClient client;
    @Override
    @Cacheable(value = "itemFeeCache", key = "#sku")
    public ItemFeesDTO requestItemFee(int sku) {
        try {
            return client.getItemFee(sku);
        } catch (FeignException exception) {
            log.error("Erro processando tributo, sku: {}", sku, exception.getCause());
            throw new FeeProcessingException(
                    exception.getMessage(),
                    exception.getCause(),
                    exception.contentUTF8()
            );
        }
    }
}
