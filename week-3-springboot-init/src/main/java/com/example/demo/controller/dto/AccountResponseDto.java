package com.example.demo.controller.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.demo.service.enums.CVCurrency;

public record AccountResponseDto(
        UUID id,
        String ownerName,
        CVCurrency currency,
        BigDecimal balance) {

}
