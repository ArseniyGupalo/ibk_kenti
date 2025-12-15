package com.example.exchanger.config.rabbit;

import lombok.Data;
import lombok.Setter;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Setter
@Service
public class ExchangeRateProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${rabbit.queue.name}")
    private String queueName;


    public void sendMessage(String message) {
        amqpTemplate.convertAndSend(queueName, message);
        System.out.println("Send message to RabbitMQ: " + message);
    }


}
