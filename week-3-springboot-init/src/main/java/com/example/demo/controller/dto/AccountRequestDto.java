package com.example.demo.controller.dto;

import java.math.BigDecimal;

import com.example.demo.service.enums.CVCurrency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountRequestDto(
        @NotBlank String ownerName,
        @NotNull CVCurrency currency,
        @NotNull BigDecimal balance) {
}
