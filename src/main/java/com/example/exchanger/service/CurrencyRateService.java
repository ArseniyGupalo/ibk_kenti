package com.example.exchanger.service;

import com.example.exchanger.dto.ExchangeRateResponse;
import com.example.exchanger.entity.CurrencyRate;
import com.example.exchanger.repository.CurrencyRateRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyRateService {
    private final CurrencyRateRepo currencyRateRepo;

    public CurrencyRateService(CurrencyRateRepo currencyRateRepo) {
        this.currencyRateRepo = currencyRateRepo;
    }

    @Transactional
    public void saveRatesToDB(ExchangeRateResponse response) {
        LocalDateTime now = LocalDateTime.now();

        response.getRates().forEach((code, rate) -> {
            CurrencyRate cr = new CurrencyRate();
            cr.setCode(code);
            cr.setBaseCurrency("USD"); // если API всегда возвращает к USD
            cr.setRate(rate);
            cr.setTimestamp(now);

            currencyRateRepo.save(cr);
        });
    }

    public List<CurrencyRate> getHistory(String code) {
        return currencyRateRepo.findByCodeOrderByTimestampAsc(code);
    }
}
