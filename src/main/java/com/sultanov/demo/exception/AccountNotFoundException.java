package com.sultanov.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String s) {
        super(s);
    }
}
