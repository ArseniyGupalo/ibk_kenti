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
        super("https://v6.exchangerate-api.com/v6/dc7805e96778c2920dd1d894/latest/USD");
    }


    @Override
    protected String getRateName() {
        return "conversion_rates";
    }
}
