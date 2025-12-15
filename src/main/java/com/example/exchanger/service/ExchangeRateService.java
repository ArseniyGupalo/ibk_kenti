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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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

    private ExchangeRateResponse fetchAllExchangeRatesFromApi() {
        logger.info("Fetching exchange rates from API`s");
        CompletableFuture<ExchangeRateResponse> fetchFromCurrencyLayerApi = currencyLayerApiService.getExchangeRate();
        CompletableFuture<ExchangeRateResponse> fetchFromOpenExchangeRateApi = openExchangeRateApiService.getExchangeRate();
        CompletableFuture<ExchangeRateResponse> fetchFromExchangeRateApi = exchangeRateApiService.getExchangeRate();

        CompletableFuture.allOf(fetchFromExchangeRateApi, fetchFromCurrencyLayerApi, fetchFromOpenExchangeRateApi).join();
        try {
           Map<String, Double> rateFromCurrencyLayerApi = fetchFromCurrencyLayerApi.get().getRates();
           Map<String, Double> rateFromOpenExchangeRateApi = fetchFromOpenExchangeRateApi.get().getRates();
           Map<String, Double> rateFromExchangeRateApi = fetchFromExchangeRateApi.get().getRates();

           Map<String, Double> mergedRates = new HashMap<>();
           if (rateFromCurrencyLayerApi != null) mergedRates.putAll(rateFromCurrencyLayerApi);
           if (rateFromOpenExchangeRateApi != null) mergedRates.putAll(rateFromOpenExchangeRateApi);
           if (rateFromExchangeRateApi != null) mergedRates.putAll(rateFromExchangeRateApi);

           logger.info("Fetched {} exchange rates from API`s", mergedRates.size());

           ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse();
           exchangeRateResponse.setRates(mergedRates);
           return exchangeRateResponse;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ExchangeRateResponse();
        }
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
