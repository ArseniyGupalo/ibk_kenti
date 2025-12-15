package com.example.exchanger.service;

import com.example.exchanger.dto.CurrencyConversionRequestDTO;
import com.example.exchanger.dto.CurrencyConversionResponseDTO;
import com.example.exchanger.dto.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Сервіс для конвертації валют.
 * Відповідає за бізнес-логіку обміну валют між двома кодами.
 */
@Service
public class CurrencyConverterService {

    // Логер для запису інформаційних повідомлень у журнал
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterService.class);

    // Сервіс для отримання актуальних курсів валют
    private final ExchangeRateService exchangeRateService;

    /**
     * Конструктор з інʼєкцією залежності ExchangeRateService
     */
    public CurrencyConverterService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Метод конвертації валюти
     *
     * @param request DTO з даними для конвертації (from, to, amount)
     * @return DTO з результатом конвертації
     */
    public CurrencyConversionResponseDTO convertCurrency(CurrencyConversionRequestDTO request) {

        // Логування вхідних параметрів
        logger.info(
                "Converting currency from {} to {}, amount {}",
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount()
        );

        // Отримання актуальних курсів валют
        ExchangeRateResponse rates = exchangeRateService.getRates();

        // Перевірка, чи підтримуються обидві валюти
        if (!rates.getRates().containsKey(request.getFromCurrency()) ||
                !rates.getRates().containsKey(request.getToCurrency())) {

            throw new IllegalArgumentException("One of currency dont supported.");
        }

        // Обчислення курсу обміну між двома валютами
        double exchangeRate =
                rates.getRates().get(request.getToCurrency()) /
                        rates.getRates().get(request.getFromCurrency());

        // Обчислення конвертованої суми
        double convertedAmount = request.getAmount() * exchangeRate;

        // Логування результатів з округленням до 2 знаків
        logger.info(
                "Current exchange rate: {} | Converted amount: {}",
                Math.round(exchangeRate * 100.0) / 100.0,
                Math.round(convertedAmount * 100.0) / 100.0
        );

        // Формування та повернення DTO з результатом
        return new CurrencyConversionResponseDTO(
                request.getFromCurrency(),
                request.getToCurrency(),
                request.getAmount(),
                Math.round(convertedAmount * 100.0) / 100.0,
                Math.round(exchangeRate * 100.0) / 100.0
        );
    }
}
