package com.sultanov.demo.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
public record AccountResponse(
        Long accountNumber,
        BigDecimal balance,
        String currencyType
) {
}
