package com.example.exchanger.controller;

import com.example.exchanger.entity.CurrencyRate;
import com.example.exchanger.service.CurrencyRateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyRateController {
    private final CurrencyRateService currencyRateService;

    public CurrencyRateController(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @GetMapping("/history/{code}")
    public List<CurrencyRate> getHistory(@PathVariable String code) {
        return currencyRateService.getHistory(code);
    }
}
