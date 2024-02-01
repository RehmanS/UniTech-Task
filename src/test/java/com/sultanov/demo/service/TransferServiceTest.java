package com.sultanov.demo.service;

import com.sultanov.demo.dto.AccountResponse;
import com.sultanov.demo.dto.TransferRequest;
import com.sultanov.demo.entity.Account;
import com.sultanov.demo.exception.AccountNotFoundException;
import com.sultanov.demo.redis.service.CurrencyCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;
    @Mock
    private AccountService accountService;
    @Mock
    private CurrencyCacheService currencyCacheService;

    @Test
    void testMakeTransfer() {

        TransferRequest request = new TransferRequest(1234567L, 12345L, BigDecimal.valueOf(100));
        AccountResponse accountResponse1 = AccountResponse.builder()
                .accountNumber(1234567L).currencyType("USD").balance(BigDecimal.valueOf(1000)).build();
        AccountResponse accountResponse2 = AccountResponse.builder()
                .accountNumber(378223L).currencyType("AZN").balance(BigDecimal.valueOf(2000)).build();

        List<AccountResponse> myActiveAccounts = Arrays.asList(accountResponse1, accountResponse2);
        Account myAccount = Account.builder().accountNumber(1234567L).state(1).balance(BigDecimal.valueOf(1000)).currencyType("USD").build();
        Account targetAccount = Account.builder().accountNumber(12345L).state(1).balance(BigDecimal.valueOf(1000)).currencyType("AZN").build();

        Mockito.when(accountService.getAllActiveAccounts()).thenReturn(myActiveAccounts);
        Mockito.when(accountService.getAccountByAccountNumber(request.myAccount())).thenReturn(myAccount);
        Mockito.when(accountService.getAccountByAccountNumber(request.targetAccount())).thenReturn(targetAccount);

        Mockito.when(currencyCacheService.readFromCache(Mockito.anyString())).thenReturn(5.0);

        transferService.transferMoney(request);
    }

    @Test
    void testMakeTransfer_AccountNotFoundException() {

        TransferRequest request = new TransferRequest(1234567L, 12345L, BigDecimal.valueOf(100));
        AccountResponse accountResponse1 = AccountResponse.builder()
                .accountNumber(1234567L).currencyType("USD").balance(BigDecimal.valueOf(1000)).build();
        AccountResponse accountResponse2 = AccountResponse.builder()
                .accountNumber(378223L).currencyType("AZN").balance(BigDecimal.valueOf(2000)).build();
        List<AccountResponse> myActiveAccounts = Arrays.asList(accountResponse1, accountResponse2);
        Account myAccount = Account.builder().accountNumber(1234567L).state(1).balance(BigDecimal.valueOf(1000)).currencyType("USD").build();

        Mockito.when(accountService.getAllActiveAccounts()).thenReturn(myActiveAccounts);
        Mockito.when(accountService.getAccountByAccountNumber(request.myAccount())).thenReturn(myAccount);
        Mockito.when(accountService.getAccountByAccountNumber(request.targetAccount())).thenReturn(null);

        assertThrows(AccountNotFoundException.class, () -> transferService.transferMoney(request));
    }

}