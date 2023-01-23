package com.example.exchange.mapper;


import com.example.exchange.entity.ExchangeRates;
import com.example.exchange.parser.CubeRate;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RateMapper {

    List<ExchangeRates> map(List<CubeRate> cube);
}