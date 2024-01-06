package com.wendersonp.processor.domain.service;

import com.wendersonp.processor.domain.models.SaleOrderEntity;

public interface SaleOrderPersistenceService {

    void save(SaleOrderEntity saleOrder);
}
