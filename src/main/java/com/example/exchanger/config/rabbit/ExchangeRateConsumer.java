package com.example.exchanger.config.rabbit;

import com.example.exchanger.service.ExchangeRateService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Setter
@Service
public class ExchangeRateConsumer {

    private final ExchangeRateService exchangeRateService;


    public ExchangeRateConsumer(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }


    @RabbitListener(queues = "${rabbit.queue.name}")
    public void receiveMessage(String message) {
        log.info("Received RabbitMQ message: {}", message);
        exchangeRateService.updateRates();
    }
}
