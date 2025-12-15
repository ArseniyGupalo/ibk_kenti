package com.example.exchanger.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CurrencyConversionRequestDTO {

    @NotNull(message = "FromCurrency cannot be null")
    private String fromCurrency;

    @NotNull(message = "ToCurrency cannot be null")
    private String toCurrency;

    @Positive(message = "Amount should be positive")
    private double amount;
}
