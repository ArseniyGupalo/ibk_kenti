package com.example.exchanger.service;

import com.example.exchanger.config.rabbit.ExchangeRateProducer;
import com.example.exchanger.dto.ExchangeRateResponse;
import com.example.exchanger.service.exchangeService.CurrencyLayerApiService;
import com.example.exchanger.service.exchangeService.ExchangeRateApiService;
import com.example.exchanger.service.exchangeService.ExchangeService;
import com.example.exchanger.service.exchangeService.OpenExchangeRateApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExchangeRateService {


    private final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);
    private final CurrencyLayerApiService currencyLayerApiService;
    private final OpenExchangeRateApiService openExchangeRateApiService;
    private final ExchangeRateApiService exchangeRateApiService;
    private final ExchangeRateProducer exchangeRateProducer;

    @Autowired
    public ExchangeRateService(CurrencyLayerApiService currencyLayerApiService, OpenExchangeRateApiService openExchangeRateApiService, ExchangeRateApiService exchangeRateApiService, ExchangeRateProducer exchangeRateProducer) {
        this.currencyLayerApiService = currencyLayerApiService;
        this.openExchangeRateApiService = openExchangeRateApiService;
        this.exchangeRateApiService = exchangeRateApiService;
        this.exchangeRateProducer = exchangeRateProducer;
    }

    private CompletableFuture<ExchangeRateResponse> safeCall(
            CompletableFuture<ExchangeRateResponse> future) {

        return future
                .orTimeout(2, TimeUnit.SECONDS)
                .exceptionally(ex -> {
                    logger.warn("API failed: {}", ex.getMessage());
                    return null;
                });
    }



    private ExchangeRateResponse fetchAllExchangeRatesFromApi() {
        logger.info("Fetching exchange rates from API`s");
        CompletableFuture<ExchangeRateResponse> fetchFromCurrencyLayerApi = safeCall(currencyLayerApiService.getExchangeRate());
        CompletableFuture<ExchangeRateResponse> fetchFromOpenExchangeRateApi = safeCall(openExchangeRateApiService.getExchangeRate());
        CompletableFuture<ExchangeRateResponse> fetchFromExchangeRateApi = safeCall(exchangeRateApiService.getExchangeRate());

        CompletableFuture.anyOf(fetchFromExchangeRateApi, fetchFromCurrencyLayerApi, fetchFromOpenExchangeRateApi).join();

        Map<String, Double> mergedRates = new HashMap<>();

        Stream.of(fetchFromCurrencyLayerApi, fetchFromOpenExchangeRateApi, fetchFromExchangeRateApi)
                .map(CompletableFuture::join)      // join безопасен после exceptionally
                .filter(Objects::nonNull)          // убираем упавшие API
                .map(ExchangeRateResponse::getRates)
                .filter(Objects::nonNull)
                .forEach(mergedRates::putAll);

        logger.info("Fetched {} exchange rates from APIs", mergedRates.size());

        ExchangeRateResponse response = new ExchangeRateResponse();
        response.setRates(mergedRates);
        return response;

    }

    @Cacheable(value = "exchangeRates")
    public ExchangeRateResponse getRates() {
        logger.info("Fetching exchange rates");
        logger.debug("Exchange rates details: {}", fetchAllExchangeRatesFromApi());
        return fetchAllExchangeRatesFromApi();
    }

    @CachePut(value = "exchangeRates")
    public ExchangeRateResponse updateRates() {
        return fetchAllExchangeRatesFromApi();
    }

    @CacheEvict(value = "exchangeRates")
    public void clearCache() {
        logger.info("Clear cache");
    }

    @Scheduled(fixedRate = 600000)
    public void refreshRates() {
        logger.info("Sending message to RabbitMQ for exchange rate update");
        exchangeRateProducer.sendMessage("Updated exchange rates");
    }

    @Cacheable(value = "exchangeSelectedRates")
    public Map<String, Double> fetchSelectedExchangeRatesFromApi(List<String> neededCurrencies) {
        ExchangeRateResponse response = fetchAllExchangeRatesFromApi();
        return response.getRates().entrySet().stream()
                .filter((entry) -> neededCurrencies.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


}
