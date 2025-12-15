package com.example.exchanger.service.exchangeService;

import com.example.exchanger.dto.ExchangeRateResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Service
public class OpenExchangeRateApiService extends ExchangeService{

    public OpenExchangeRateApiService() {
        super("https://openexchangerates.org/api/latest.json?app_id=8bc459f20137490da46facf2f5a72d79");
    }


    @Override
    protected String getRateName() {
        return "rates";
    }
}
