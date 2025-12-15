package com.example.exchanger.service;

import com.example.exchanger.dto.CurrencyConversionRequestDTO;
import com.example.exchanger.dto.CurrencyConversionResponseDTO;
import com.example.exchanger.dto.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CurrencyConverterService {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterService.class);
    private final ExchangeRateService exchangeRateService;

    public CurrencyConverterService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public CurrencyConversionResponseDTO convertCurrency(CurrencyConversionRequestDTO request) {
        logger.info("Converting currency from {} to {}, amount {}", request.getFromCurrency(), request.getToCurrency(),request.getAmount());
        ExchangeRateResponse rates = exchangeRateService.getRates();

        if (!rates.getRates().containsKey(request.getFromCurrency()) ||
                !rates.getRates().containsKey(request.getToCurrency())) {
            throw new IllegalArgumentException("One of currency dont supported.");
        }

        double exchangeRate = rates.getRates().get(request.getToCurrency())
                / rates.getRates().get(request.getFromCurrency());
        double convertedAmount = request.getAmount() * exchangeRate;

        logger.info("Current exchange rate: {} | Converted amount: {}", Math.round(exchangeRate * 100.0) / 100.0, Math.round(convertedAmount * 100.0) / 100.0);

        return new CurrencyConversionResponseDTO(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount(),
                Math.round(convertedAmount * 100.0) / 100.0,
                Math.round(exchangeRate * 100.0) / 100.0
        );
    }
}
