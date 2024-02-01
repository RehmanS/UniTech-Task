package com.sultanov.demo.dto;

import java.math.BigDecimal;

public record TransferRequest(
        Long myAccount,
        Long targetAccount,
        BigDecimal amount
) {
}
