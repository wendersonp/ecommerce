package com.wendersonp.processor.domain.service;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.receiver.domain.dto.ItemDTO;

import java.util.List;

public interface ItemFeesService {

    List<ItemFeesDTO> getFeesForItems(List<ItemDTO> items);
}
