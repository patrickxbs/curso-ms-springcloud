package com.patrick.book_service.dto;

import java.math.BigDecimal;

public record ExchangeDto(Long id, String from, String to, BigDecimal conversionFactor, Double convertedValue, String environment) {
}
