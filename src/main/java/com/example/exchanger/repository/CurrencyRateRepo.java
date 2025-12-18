package com.example.exchanger.repository;

import com.example.exchanger.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrencyRateRepo extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> findByCodeOrderByTimestampAsc(String code);
    CurrencyRate findTopByCodeOrderByTimestampDesc(String code);
}
