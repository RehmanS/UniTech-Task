package com.sultanov.demo.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCodeEnum {

    ACCOUNT_NOT_FOUND(1001, "Target account does not exist"),
    INVALID_ACCOUNT(1002, "This account is inactive"),
    SAME_ACCOUNT(1003, "You cannot send money to the same account"),
    INSUFFICIENT_FUNDS(1004, "Insufficient Funds"),
    TRANSFER_NOT_ALLOWED(1005, "You can only transfer money from your own account!"),
    DEPOSIT_NOT_ALLOWED(1006, "You can only deposit money into your own account"),
    USER_NOT_FOUND(1007, "User not found");

    private final int code;
    private final String message;

}
