package com.sultanov.demo.controller;

import com.sultanov.demo.dto.AccountCreateRequest;
import com.sultanov.demo.dto.AccountResponse;
import com.sultanov.demo.dto.DepositInfo;
import com.sultanov.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping()
    public List<AccountResponse> getAllActiveAccounts() {
        return accountService.getAllActiveAccounts();
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody AccountCreateRequest request) {
        accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account successfully created");
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositFunds(@RequestBody DepositInfo depositInfo) {
        accountService.depositFunds(depositInfo);
        return ResponseEntity.status(HttpStatus.OK).body("Money has been successfully added");
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<String> deleteAccountByAccountNumber(@PathVariable Long accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.status(HttpStatus.OK).body("Account has been successfully deleted");
    }

}
