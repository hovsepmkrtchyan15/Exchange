package com.example.exchange.service;

import com.example.exchange.dto.ExchangeDto;
import com.example.exchange.entity.ExchangeRates;
import com.example.exchange.faign.CEBClient;
import com.example.exchange.mapper.RateMapper;
import com.example.exchange.parser.CubeRate;
import com.example.exchange.parser.Envelope;
import com.example.exchange.repository.ExchangeRatesRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeService {

    private final ExchangeRatesRepository exchangeRatesRepository;

    private final CEBClient cebClient;
    private final RateMapper mapper;


    public List<ExchangeRates> findAllByDate(LocalDate date) {
        Optional<List<ExchangeRates>> allByDate = exchangeRatesRepository.findAllByDate(date);
        if (allByDate.isPresent() && allByDate.get().size() == 0) {
            saveRates();
            findAllByDate(date);
        }
        return allByDate.get();
    }

    public void saveRates() {
        try {
            String xml = cebClient.getSbpBankIcons();
            StringReader sr = new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(Envelope.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Envelope response = (Envelope) unmarshaller.unmarshal(sr);
            List<CubeRate> cubes = response.getCube().getCubeTime().getCubes();
            List<ExchangeRates> exchangeRates = mapper.map(cubes);
            for (ExchangeRates exchangeRate : exchangeRates) {
                exchangeRate.setDate(LocalDate.now());
            }
            exchangeRatesRepository.saveAll(exchangeRates);

        } catch (JAXBException e) {
            log.error("Unable get and parse rates for: {}", LocalDate.now());
        }
    }

    private BigDecimal change(int idFrom, int idTo) throws NotFoundException {

        Optional<ExchangeRates> byIdFrom = exchangeRatesRepository.findById(idFrom);
        Optional<ExchangeRates> byIdTo = exchangeRatesRepository.findById(idTo);
        if (byIdFrom.isPresent() && byIdTo.isPresent()) {
            return (byIdTo.get().getRate().divide((byIdFrom.get().getRate()), 3, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
        }
        log.error("Currency whit idFrom = {} or idTo = {} not found", idFrom, idTo);
        throw new NotFoundException("Currency whit id = " + idFrom + " or " + idTo + " does not exists");
    }

    private String findById(int id) throws NotFoundException {
        Optional<ExchangeRates> byId = exchangeRatesRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get().getCurrency();
        }
        log.error("Currency whit id = {} not found", id);
        throw new NotFoundException("Currency whit id = " + id + " does not exists");
    }

    public ExchangeDto total(int idFrom, int idTo, BigDecimal sum) {
        try {
            BigDecimal rate = change(idFrom, idTo);
            String currencyNameFrom = findById(idFrom);
            String currencyNameTo = findById(idTo);
            BigDecimal total = (rate.multiply(sum)).setScale(3, RoundingMode.HALF_UP);
            return ExchangeDto.builder()
                    .currencyNameFrom(currencyNameFrom)
                    .currencyNameTo(currencyNameTo)
                    .total(total)
                    .rate(rate)
                    .build();
        } catch (NotFoundException e) {
            log.error("Currency whit idFrom = {} or idTo = {} not found", idFrom, idTo);
            return null;
        }
    }
}
