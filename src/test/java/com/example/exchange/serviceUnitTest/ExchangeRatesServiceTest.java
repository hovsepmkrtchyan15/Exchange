package com.example.exchange.serviceUnitTest;

import com.example.exchange.entity.ExchangeRates;
import com.example.exchange.faign.CEBClient;
import com.example.exchange.mapper.RateMapper;
import com.example.exchange.parser.CubeRate;
import com.example.exchange.repository.ExchangeRatesRepository;
import com.example.exchange.service.ExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExchangeRatesServiceTest {

    @MockBean
    private ExchangeRatesRepository exchangeRatesRepository;


    @Autowired
    private ExchangeService exchangeService;

    @Test
    public void findAllByDate_returnsExchangeRatesForGivenDate() {
        LocalDate date = LocalDate.of(2023, 1, 10);
        ExchangeRates exchangeRate1 = new ExchangeRates(1, "use", new BigDecimal(4), date);
        ExchangeRates exchangeRate2 = new ExchangeRates(2, "eur", new BigDecimal(2), date);
        List<ExchangeRates> expectedExchangeRates = Arrays.asList(exchangeRate1, exchangeRate2);

        when(exchangeRatesRepository.findAllByDate(date)).thenReturn(Optional.of(expectedExchangeRates));

        List<ExchangeRates> actualExchangeRates = exchangeService.findAllByDate(date);

        assertEquals(expectedExchangeRates, actualExchangeRates);
    }

    @Test
    public void findAllByDate_Null() {
        LocalDate date = LocalDate.of(2023, 1, 10);
        ExchangeRates exchangeRate1 = new ExchangeRates();
        ExchangeRates exchangeRate2 = new ExchangeRates();
        List<ExchangeRates> expectedExchangeRatesNull = Arrays.asList(exchangeRate1, exchangeRate2);

        when(exchangeRatesRepository.findAllByDate(date)).thenReturn(Optional.of(expectedExchangeRatesNull));

        verify(exchangeRatesRepository, times(1)).saveAll(any());

    }
}




