package com.example.exchanger.controller;


import com.example.exchanger.dto.ExchangeRateResponse;
import com.example.exchanger.service.ExchangeRateService;
import com.example.exchanger.service.exchangeService.CurrencyLayerApiService;
import com.example.exchanger.service.exchangeService.ExchangeRateApiService;
import com.example.exchanger.service.exchangeService.OpenExchangeRateApiService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


@SpringBootTest
class ExchangeRateControllerTest {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateControllerTest.class);


    private WebClient webClient;


    @Autowired
    private ExchangeRateController exchangeRateController; // Используем реальный контроллер

    @Autowired
    private ExchangeRateApiService exchangeRateApiService;

    @Autowired
    private OpenExchangeRateApiService openExchangeRateApiService;

    @Autowired
    private CurrencyLayerApiService currencyLayerApiService;


    @Test
    void getAllExchangeRatesNotNull() throws ExecutionException, InterruptedException {
        CompletableFuture<ExchangeRateResponse> exchangeRateApiServiceRates = exchangeRateController.getExchangeRateFromExchangeRateApi();
        assertNotNull(exchangeRateApiServiceRates);
        logger.info("exchangeRateApiServiceRates {}", exchangeRateApiServiceRates.get().toString());


        CompletableFuture<ExchangeRateResponse> openExchangeRateApiServiceRates = exchangeRateController.getExchangeRateFromOpenApi();
        assertNotNull(openExchangeRateApiServiceRates);
        logger.info("openExchangeRateApiServiceRates {}", openExchangeRateApiServiceRates.get().toString());

        CompletableFuture<ExchangeRateResponse> currencyLayerApiServiceRates = exchangeRateController.getExchangeRateFromCurrencyLayer();
        assertNotNull(currencyLayerApiServiceRates);
        logger.info("currencyLayerApiServiceRates {}", currencyLayerApiServiceRates.get().toString());

    }


}
