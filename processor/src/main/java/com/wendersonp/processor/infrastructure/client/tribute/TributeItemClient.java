package com.wendersonp.processor.infrastructure.client.tribute;

import com.wendersonp.processor.domain.dto.ItemFeesDTO;
import com.wendersonp.processor.infrastructure.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "tribute-item-client", url = "${client.external.tribute-item-url}", configuration = FeignConfiguration.class)
public interface TributeItemClient {

    @GetMapping("/tributo")
    ItemFeesDTO getItemFee(@RequestParam int sku);
}
