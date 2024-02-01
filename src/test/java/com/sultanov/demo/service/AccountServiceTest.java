package com.sultanov.demo.service;

import com.sultanov.demo.dto.AccountCreateRequest;
import com.sultanov.demo.dto.AccountResponse;
import com.sultanov.demo.dto.DepositInfo;
import com.sultanov.demo.entity.Account;
import com.sultanov.demo.entity.User;
import com.sultanov.demo.repository.AccountRepository;
import com.sultanov.demo.security.security.JwtUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserService userService;

    @Test
    void testCreateAccount() {
        AccountCreateRequest request = new AccountCreateRequest(1234567L, BigDecimal.valueOf(1000), "USD");
        String pin = "mockedPin";
        User user = User.builder().id(1L).pin("mockedPin").password("1232ss").build();

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(new JwtUserDetails(1L, pin, "12345", null));
        when(accountService.getPinFromToken()).thenReturn(pin);
        when(userService.getOneUserByUserPin(pin)).thenReturn(user);

        accountService.createAccount(request);

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccount() {
        Account targetAccount = new Account(1234567L, 1, BigDecimal.valueOf(100), "USD", new User());
        Account myAccount = new Account(7654321L, 1, BigDecimal.valueOf(200), "USD", new User());

        accountService.updateBalanceAfterTransfer(targetAccount, myAccount);

        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void testGetAllActiveAccounts() {
        String pin = "mockedPin";
        Authentication authentication = mock(Authentication.class);
        JwtUserDetails userDetails = new JwtUserDetails(1L, pin, "12345", null);
        List<Account> accounts = new ArrayList<>();
        Account account = new Account(1234567L, 1, BigDecimal.valueOf(100), "USD", new User());
        Account account1 = new Account(7654321L, 1, BigDecimal.valueOf(200), "USD", new User());
        accounts.add(account1);
        accounts.add(account);
        List<AccountResponse> expectedResponses = new ArrayList<>();
        AccountResponse accountResponse = new AccountResponse(1234567L, BigDecimal.valueOf(100), "USD");
        AccountResponse accountResponse1 = new AccountResponse(7654321L, BigDecimal.valueOf(2100), "AZN");

        when(accountService.getPinFromToken()).thenReturn(pin);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(accountRepository.findAccountsByUserPin(pin)).thenReturn(accounts);
        when(accountService.mapAccountToAccountResponse(any(Account.class))).thenCallRealMethod();

        List<AccountResponse> actualResponses = accountService.getAllActiveAccounts();

        assertEquals(expectedResponses.size(), actualResponses.size());
        for (int i = 0; i < expectedResponses.size(); i++) {
            assertEquals(expectedResponses.get(i), actualResponses.get(i));
        }
    }

    @Test
    void testDepositFunds() {
        String pinFromToken = "1234";
        Long accountNumber = 1234567890L;
        BigDecimal depositAmount = BigDecimal.valueOf(100);

        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.setUser(new User(1L, pinFromToken, "12345", null)); // Assuming User has a pin property

        when(accountService.getPinFromToken()).thenReturn(pinFromToken);
        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(Optional.of(mockAccount));

        accountService.depositFunds(new DepositInfo(accountNumber, depositAmount));

        Mockito.verify(accountRepository).save(any(Account.class));

        assertEquals(mockAccount.getBalance(), BigDecimal.valueOf(100).add(depositAmount));
    }

    @Test
    public void testDeleteAccount() {
        Long accountNumber = 123L;
        String pin = "1234";

        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        mockAccount.getUser().setPin(pin);

        when(accountRepository.findAccountByAccountNumber(accountNumber)).thenReturn(java.util.Optional.of(mockAccount));
        when(accountService.getPinFromToken()).thenReturn(pin);

        accountService.deleteAccount(accountNumber);
        verify(mockAccount).setState(0);
        verify(accountRepository).save(mockAccount);
    }
}