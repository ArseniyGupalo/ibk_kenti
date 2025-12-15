package com.example.exchanger.service.exchangeService;

import com.example.exchanger.dto.ExchangeRateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public abstract class ExchangeService {

    private final WebClient webClient;

    private final String url;

    public ExchangeService(String url) {
        this.webClient = WebClient.builder().build();
        this.url = url;
    }

    @Async
    public CompletableFuture<ExchangeRateResponse> getExchangeRate() {

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ExchangeRateResponse.class)
                .map(this::normalizeRateResponse)
                .toFuture();
    }

    private ExchangeRateResponse normalizeRateResponse(ExchangeRateResponse response) {
        if (response == null || response.getRates() == null || response.getRates().isEmpty() || response.getRates().get("USD") != null) {
            return response;
        }
        Map<String, Double> newRates = new HashMap<>();
        for (Map.Entry<String, Double> entry : response.getRates().entrySet()) {
            String key = entry.getKey().replace("USD", "");
            newRates.put(key, entry.getValue());
        }
        response.setRates(newRates);
        return response;
    }

    protected abstract String getRateName();


}
