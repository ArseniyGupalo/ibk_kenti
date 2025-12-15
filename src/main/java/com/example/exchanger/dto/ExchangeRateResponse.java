package com.example.exchanger.dto;


import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Data
public class ExchangeRateResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;


    @NotNull(message = "Exchange rates cannot be null")
    @JsonAlias({"conversion_rates", "rates", "quotes"})
    Map<String, Double> rates;


//    public void normalizeRates() {
//        if (rates == null) {return ;}
//
//        Map<String, Double> newRates = new HashMap<>();
//        for (Map.Entry<String, Double> entry : rates.entrySet()) {
//            String key = entry.getKey().replace("USD", "");
//            Double value = entry.getValue();
//            newRates.put(key, value);
//        }
//        rates = newRates;
//
//
//    }
}
