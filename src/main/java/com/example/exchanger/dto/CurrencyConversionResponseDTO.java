package com.example.exchanger.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyConversionResponseDTO {
    @NotNull(message = "FromCurrency cannot be null")
    private String fromCurrency;

    @NotNull(message = "ToCurrency cannot be null")
    private String toCurrency;

    @Positive(message = "Amount should be positive")
    private double amount;

    @Positive(message = "ConvertedAmount should be positive")
    private double convertedAmount;

    @NotNull(message = "Exchange rate cannot be null")
    private double exchangeRate;
}
