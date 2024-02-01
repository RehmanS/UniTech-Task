package com.sultanov.demo.redis.service;

import com.sultanov.demo.redis.entity.CurrencyRate;
import com.sultanov.demo.redis.repository.CurrencyRateRepository;
import com.sultanov.demo.service.CurrencyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyCacheService {

    private final CurrencyRateRepository currencyRateRepository;
    private final CurrencyService currencyService;

    @Transactional
    public void saveToCache(String currencyCode, Double currency) {
        CurrencyRate currencyRate = CurrencyRate.builder()
                .currencyCode(currencyCode)
                .currency(currency)
                .updatedTime(LocalDateTime.now())
                .build();
        currencyRateRepository.save(currencyRate);
    }

    public Double readFromCache(String currencyCode) {
        CurrencyRate currencyRate = currencyRateRepository.findById(currencyCode).orElse(null);

        if (currencyRate == null) {
            Double currencyFromThirdParty = currencyService.getCurrenciesFromThirdParty(currencyCode);
            saveToCache(currencyCode, currencyFromThirdParty);
            return currencyFromThirdParty;
        } else {
            if (isWithinOneMinute(currencyRate.getUpdatedTime())) {
                log.info("--- cache den ---");
                return currencyRate.getCurrency();
            } else {
                Double currencyFromThirdParty = currencyService.getCurrenciesFromThirdParty(currencyCode);
                saveToCache(currencyCode, currencyFromThirdParty);
                return currencyFromThirdParty;
            }
        }
    }

    private boolean isWithinOneMinute(LocalDateTime lastUpdate) {
        if (lastUpdate == null) {
            return false;
        }
        LocalDateTime currentTimestamp = LocalDateTime.now();
        long minutesDifference = ChronoUnit.MINUTES.between(lastUpdate, currentTimestamp);
        return minutesDifference < 1;
    }
}
