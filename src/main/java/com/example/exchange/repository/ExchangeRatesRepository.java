package com.example.exchange.repository;


import com.example.exchange.entity.ExchangeRates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRatesRepository extends JpaRepository<ExchangeRates, Integer> {

    Optional<List<ExchangeRates>> findAllByDate(LocalDate date);




}
