package com.example.exchanger.service.exchangeService;

import com.example.exchanger.dto.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
public class ExchangeRateApiService extends ExchangeService {



    public ExchangeRateApiService() {
        super("https://v6.exchangerate-api.com/v6/30ab173f9c765377a204b64e/latest/USD");
    }


    @Override
    protected String getRateName() {
        return "conversion_rates";
    }
}
