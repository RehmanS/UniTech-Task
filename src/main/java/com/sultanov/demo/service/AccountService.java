package com.sultanov.demo.service;

import com.sultanov.demo.dto.AccountCreateRequest;
import com.sultanov.demo.dto.AccountResponse;
import com.sultanov.demo.dto.DepositInfo;
import com.sultanov.demo.entity.Account;
import com.sultanov.demo.entity.User;
import com.sultanov.demo.enums.ErrorCodeEnum;
import com.sultanov.demo.exception.CustomException;
import com.sultanov.demo.repository.AccountRepository;
import com.sultanov.demo.security.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    public void createAccount(AccountCreateRequest request) {
        String pin = getPinFromToken();
        User user = ofNullable(userService.getOneUserByUserPin(pin))
                .orElseThrow(() -> new CustomException(ErrorCodeEnum.USER_NOT_FOUND));

        var account = Account
                .builder()
                .accountNumber(request.accountNumber())
                .user(user)
                .state(1)
                .balance(request.balance())
                .currencyType(request.currencyType())
                .build();

        accountRepository.save(account);
    }

    @Transactional
    public void updateBalanceAfterTransfer(Account targetAccount, Account myAccount) {
        accountRepository.save(targetAccount);
        accountRepository.save(myAccount);
    }

    public void depositFunds(DepositInfo depositInfo) {
        String pin = getPinFromToken();

        Account account = accountRepository
                .findAccountByAccountNumber(depositInfo.accountNumber())
                .filter(acc -> acc.getUser().getPin().equals(pin))
                .orElseThrow(() -> new CustomException(ErrorCodeEnum.DEPOSIT_NOT_ALLOWED));

        account.setBalance(account.getBalance().add(depositInfo.amount()));
        accountRepository.save(account);
    }

    public List<AccountResponse> getAllActiveAccounts() {
        var pin = getPinFromToken();
        log.info("----pin: {}", pin);
        return accountRepository
                .findAccountsByUserPin(pin)
                .stream()
                .filter(account -> account.getState()==1)
                .map(this::mapAccountToAccountResponse)
                .toList();
    }

    public Account getAccountByAccountNumber(Long accountNumber) {
        return accountRepository
                .findAccountByAccountNumber(accountNumber)
                .orElseThrow();
    }

    public void deleteAccount(Long accountNumber) {
        var account = accountRepository
                .findAccountByAccountNumber(accountNumber)
                .filter(acc -> acc.getUser().getPin().equals(getPinFromToken()))
                .orElseThrow(() -> new CustomException(ErrorCodeEnum.ACCOUNT_NOT_FOUND));

        account.setState(0);
        accountRepository.save(account);
    }

    AccountResponse mapAccountToAccountResponse(Account account) {
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .currencyType(account.getCurrencyType())
                .build();
    }

    protected String getPinFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((JwtUserDetails) authentication.getPrincipal()).getUsername();
    }
}
