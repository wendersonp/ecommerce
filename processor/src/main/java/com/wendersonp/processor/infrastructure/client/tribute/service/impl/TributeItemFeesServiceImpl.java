package com.wendersonp.processor.infrastructure.client.tribute.service.impl;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.domain.service.ItemFeesService;
import com.wendersonp.processor.infrastructure.client.tribute.service.TributeItemConnector;
import com.wendersonp.receiver.domain.dto.ItemDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TributeItemFeesServiceImpl implements ItemFeesService {

    private final TributeItemConnector connector;
    @Override
    public List<ItemFeesDTO> getFeesForItems(List<ItemDTO> items) {
        return items
                .parallelStream()
                .map(ItemDTO::getSku)
                .map(connector::requestItemFee)
                .toList();

    }
}
