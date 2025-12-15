package com.example.exchanger.controller;

import com.example.exchanger.dto.ExchangeRateResponse;
import com.example.exchanger.service.ExchangeRateService;
import com.example.exchanger.service.exchangeService.CurrencyLayerApiService;
import com.example.exchanger.service.exchangeService.ExchangeRateApiService;
import com.example.exchanger.service.exchangeService.OpenExchangeRateApiService;
import com.example.exchanger.config.rabbit.ExchangeRateProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    private final ExchangeRateApiService exchangeRateApiService;
    private final OpenExchangeRateApiService openExchangeRateApiService;
    private final CurrencyLayerApiService currencyLayerApiService;
    private final ExchangeRateProducer exchangeRateProducer;
    private final Logger logger = LoggerFactory.getLogger(ExchangeRateController.class);


    public ExchangeRateController(ExchangeRateService exchangeRateService, ExchangeRateApiService exchangeRateApiService, OpenExchangeRateApiService openExchangeRateApiService, CurrencyLayerApiService currencyLayerApiService, ExchangeRateProducer exchangeRateProducer) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateApiService = exchangeRateApiService;
        this.openExchangeRateApiService = openExchangeRateApiService;
        this.currencyLayerApiService = currencyLayerApiService;
        this.exchangeRateProducer = exchangeRateProducer;
    }

    @GetMapping("/rates")
    public ResponseEntity<ExchangeRateResponse> getRates() {
        return ResponseEntity.ok(exchangeRateService.getRates());
    }

    @PostMapping("/rates/update")
    public ResponseEntity<ExchangeRateResponse> updateRates() {
        exchangeRateProducer.sendMessage("rates are updated normally!");
        return ResponseEntity.ok(exchangeRateService.updateRates());
    }

    @DeleteMapping("/clear-cache")
    public ResponseEntity<String> clearCache() {
        exchangeRateService.clearCache();
        return ResponseEntity.ok("Cache have been cleared.");
    }

    @GetMapping("/exrateapi")
    public CompletableFuture<ExchangeRateResponse> getExchangeRateFromExchangeRateApi() {
        return exchangeRateApiService.getExchangeRate();
    }

    @GetMapping("/openapitest")
    public CompletableFuture<ExchangeRateResponse> getExchangeRateFromOpenApi() {
        return openExchangeRateApiService.getExchangeRate();
    }

    @GetMapping("/currnecylayerapitest")
    public CompletableFuture<ExchangeRateResponse> getExchangeRateFromCurrencyLayer() {
        return currencyLayerApiService.getExchangeRate();
    }




}
