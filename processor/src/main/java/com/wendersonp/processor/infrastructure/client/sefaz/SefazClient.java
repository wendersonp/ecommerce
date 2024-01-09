package com.wendersonp.processor.infrastructure.client.sefaz;

import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazInvoiceResponseDTO;
import com.wendersonp.processor.infrastructure.client.sefaz.dto.SefazRequestOrderDTO;
import com.wendersonp.processor.infrastructure.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "sefaz-client",
        url = "${client.external.sefaz-url}",
        configuration = FeignConfiguration.class,
        primary = false
)
public interface SefazClient {

    @PostMapping("/authorize")
    SefazInvoiceResponseDTO authorizeOrder(@RequestBody SefazRequestOrderDTO order);
}
