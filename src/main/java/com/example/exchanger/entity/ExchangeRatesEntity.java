package com.example.exchanger.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ExchangeRates")
public class ExchangeRatesEntity {
    @Id
    private Long id;

}
