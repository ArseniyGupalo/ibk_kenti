package com.example.exchanger.service.exchangeService;

import com.example.exchanger.dto.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CurrencyLayerApiService extends ExchangeService {


    public CurrencyLayerApiService() {
        super("http://apilayer.net/api/live?access_key=e651a3d4ec05d47462f5931f5351415e");
    }


    @Override
    protected String getRateName() {
        return "quotes";
    }
}
