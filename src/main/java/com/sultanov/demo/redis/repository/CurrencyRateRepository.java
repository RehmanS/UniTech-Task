package com.sultanov.demo.redis.repository;

import com.sultanov.demo.redis.entity.CurrencyRate;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRateRepository extends CrudRepository<CurrencyRate,String> {


}
