package com.sultanov.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyServiceTest {

    @Test
    void testGetCurrenciesFromThirdParty() {
        CurrencyService currencyService = new CurrencyService();

        Double usdToTryRate = currencyService.getCurrenciesFromThirdParty("USD-TRY");
        assertEquals(30.37, usdToTryRate);

        Double tryToUsdRate = currencyService.getCurrenciesFromThirdParty("TRY-USD");
        assertEquals(0.033, tryToUsdRate);

        Double invalidRate = currencyService.getCurrenciesFromThirdParty("XYZ-ABC");
        assertNull(invalidRate);
    }

}