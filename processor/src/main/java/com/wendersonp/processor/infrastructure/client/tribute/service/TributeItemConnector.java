package com.wendersonp.processor.infrastructure.client.tribute.service;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;

public interface TributeItemConnector {

    ItemFeesDTO requestItemFee(int sku);
}
