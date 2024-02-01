package com.sultanov.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class CurrencyService {

    // Mock Service
    public Double getCurrenciesFromThirdParty(String currencyCode){
        log.info("--- third party service ---");
        HashMap<String, Double> currencies = new HashMap<>();
        currencies.put("USD-TRY", 30.37);
        currencies.put("TRY-USD", 0.033);
        currencies.put("AZN-USD", 0.59);
        currencies.put("USD-AZN", 1.70);
        currencies.put("TRY-AZN", 0.056);
        currencies.put("AZN-TRY", 17.86);

        return currencies.get(currencyCode);
    }
}
