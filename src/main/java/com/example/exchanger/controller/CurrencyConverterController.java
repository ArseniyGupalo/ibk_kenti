package com.example.exchanger.controller;

import com.example.exchanger.dto.CurrencyConversionRequestDTO;
import com.example.exchanger.dto.CurrencyConversionResponseDTO;
import com.example.exchanger.service.CurrencyConverterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "*")
@Validated
public class CurrencyConverterController {
    private final CurrencyConverterService currencyConverterService;

    public CurrencyConverterController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @PostMapping("/convert")
    public ResponseEntity<CurrencyConversionResponseDTO> convert(@Valid @RequestBody CurrencyConversionRequestDTO request) {
        return ResponseEntity.ok(currencyConverterService.convertCurrency(request));
    }
}
