package com.wendersonp.processor.infrastructure.client.channel;

import com.wendersonp.processor.domain.dto.InvoiceDTO;
import com.wendersonp.processor.infrastructure.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "channel-client", url = "${client.external.channel-url}", configuration = FeignConfiguration.class)
public interface ChannelClient {

    @PostMapping(value = "/callback-venda", produces = "text/plain")
    String communicateInvoice(@RequestBody InvoiceDTO invoice);
}
