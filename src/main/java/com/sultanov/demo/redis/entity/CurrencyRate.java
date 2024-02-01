package com.sultanov.demo.redis.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash(value = "CurrencyRate", timeToLive = 61L)
@Builder
@Data
public class CurrencyRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String currencyCode;

    Double currency;
    LocalDateTime updatedTime;
}
