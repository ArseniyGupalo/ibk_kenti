package com.example.exchanger.service.exchangeService;


import com.example.exchanger.service.ExchangeRateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExchangeRateApiServiceTest {

    private final Logger logger = LoggerFactory.getLogger(ExchangeRateApiServiceTest.class);

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ExchangeRateApiService exchangeRateApiService;

    @Test
    void testUrlValue() {
        assertEquals("https://v6.exchangerate-api.com/v6/dc7805e96778c2920dd1d894/latest/USD", exchangeRateApiService);
    }

    @Test
    void testExchangeRate() throws ExecutionException, InterruptedException {
        assertNotNull(exchangeRateApiService.getExchangeRate());
        assertNotEquals(CompletableFuture.class, exchangeRateApiService.getExchangeRate().getClass());
        logger.info(exchangeRateApiService.getExchangeRate().getClass().getName());
        logger.info(String.valueOf(exchangeRateApiService.getExchangeRate().get()));
    }
}
