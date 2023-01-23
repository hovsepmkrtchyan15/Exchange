package com.example.exchange.dto;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ExchangeDto {

    private BigDecimal rate;
    private String currencyNameFrom;
    private String currencyNameTo;
    private BigDecimal total;
}
