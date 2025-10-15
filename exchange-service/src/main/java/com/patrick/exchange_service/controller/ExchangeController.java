package com.patrick.exchange_service.controller;

import com.patrick.exchange_service.environment.InstanceInformationService;
import com.patrick.exchange_service.model.Exchange;
import com.patrick.exchange_service.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RequiredArgsConstructor
@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    private final ExchangeRepository exchangeRepository;
    private final InstanceInformationService instanceInformationService;

    // http://localhost:8765/exchange/10/USD/BRL
    @GetMapping(value = "/{amount}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(
            @PathVariable BigDecimal amount,
            @PathVariable String from,
            @PathVariable String to) {

        logger.info("getExchange is called with -> {}, {} and {}", amount, from, to);
        Exchange exchange = exchangeRepository.findByFromAndTo(from, to);

        if (exchange == null) throw new RuntimeException("Currency Unsupported!");

        BigDecimal conversionFactor = exchange.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        exchange.setConvertedValue(convertedValue);
        exchange.setEnvironment("PORT " + instanceInformationService.retrieveServerPort());

        return exchange;
    }
}
