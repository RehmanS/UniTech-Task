package com.sultanov.demo.dto;

import java.math.BigDecimal;

public record DepositInfo(
        Long accountNumber,
        BigDecimal amount
) {
}
