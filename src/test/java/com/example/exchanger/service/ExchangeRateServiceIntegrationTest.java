package com.example.exchanger.service;

import com.example.exchanger.service.exchangeService.CurrencyLayerApiService;
import com.example.exchanger.service.exchangeService.ExchangeRateApiService;
import com.example.exchanger.service.exchangeService.OpenExchangeRateApiService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ExchangeRateServiceIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateServiceIntegrationTest.class);

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Autowired
    private CurrencyLayerApiService currencyLayerApiService;

    @Autowired
    private ExchangeRateApiService exchangeRateApiService;

    @Autowired
    private OpenExchangeRateApiService openExchangeRateApiService;

    @Test
    void getAllExchangeRatesFromAllApi() {
        assertNotNull(exchangeRateService.getRates());
        logger.info(exchangeRateService.getRates().toString());
    }

    @Test
    void testPerformance() throws Exception {
        long startAsync = System.currentTimeMillis();
        exchangeRateService.getRates(); // Асинхронный метод
        long endAsync = System.currentTimeMillis();
        long asyncTime = endAsync - startAsync;

        long startSync = System.currentTimeMillis();
        currencyLayerApiService.getExchangeRate().get();  // Последовательные вызовы
        openExchangeRateApiService.getExchangeRate().get();
        exchangeRateApiService.getExchangeRate().get();
        long endSync = System.currentTimeMillis();
        long syncTime = endSync - startSync;

        logger.info("Async execution time: {} ms", asyncTime);
        logger.info("Sync execution time: {} ms", syncTime);

        assertTrue(asyncTime < syncTime, "Асинхронный метод должен работать быстрее синхронного");
    }




}

