package com.example.exchanger.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
public class ExchangeRateResponseForExchangeRateApi implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "Exchange rates cannot be null")
    Map<String, Double> conversion_rates;

}
