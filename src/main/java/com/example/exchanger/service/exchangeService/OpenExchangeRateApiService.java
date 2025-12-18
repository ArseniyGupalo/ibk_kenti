package com.example.exchanger.service.exchangeService;

import com.example.exchanger.dto.ExchangeRateResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
public class OpenExchangeRateApiService extends ExchangeService{

    public OpenExchangeRateApiService() {
        super("https://openexchangerates.org/api/latest.json?app_id=f4c1bab9d6ad4a8887e879b9811860f3");
    }


    @Override
    protected String getRateName() {
        return "rates";
    }
}
