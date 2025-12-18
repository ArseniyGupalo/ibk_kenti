package com.example.exchanger.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "currency_rates")
@Getter
@Setter
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @Column(name = "base_currency")
    private String baseCurrency;

    private Double rate;

    private LocalDateTime timestamp;
}
