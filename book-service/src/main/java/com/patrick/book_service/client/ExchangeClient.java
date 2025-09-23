package com.patrick.book_service.client;

import com.patrick.book_service.dto.ExchangeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchange-service")
public interface ExchangeClient {

    @GetMapping(value = "/exchange/{amount}/{from}/{to}")
    ExchangeDto getExchange(
            @PathVariable Double amount,
            @PathVariable String from,
            @PathVariable String to);
}
