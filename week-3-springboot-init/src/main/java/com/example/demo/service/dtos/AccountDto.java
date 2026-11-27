package com.example.demo.service.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.demo.service.enums.CVCurrency;

public record AccountDto(
        UUID id,
        String ownerName,
        CVCurrency currency,
        BigDecimal balance) {
}
