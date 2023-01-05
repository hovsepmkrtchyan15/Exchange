package com.example.exchange.service;

import com.example.exchange.entity.Log;
import com.example.exchange.repository.LogRepository;
import com.example.exchange.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void saveLog(String currencyNameFrom, String currencyNameTo, Double rate, CurrentUser user) {
        Log log = Log.builder()
                .user(user.getUser())
                .rate(rate)
                .currencyNameFrom(currencyNameFrom)
                .currencyNameTo(currencyNameTo)
                .date(LocalDate.now())
                .build();
        logRepository.save(log);
    }

    public Page<Log> findByDate(String dateAfter, String dateBefore, Pageable pageable) {

        if (!StringUtils.isEmpty(dateAfter) && StringUtils.isEmpty(dateBefore)) {
            return logRepository.findLogByDateAfter(LocalDate.parse(dateAfter), pageable);

        } else if (StringUtils.isEmpty(dateAfter) && !StringUtils.isEmpty(dateBefore)) {
            return logRepository.findLogByDateBefore(LocalDate.parse(dateBefore), pageable);

        } else if (!StringUtils.isEmpty(dateAfter) && !StringUtils.isEmpty(dateBefore)) {
            return logRepository.findLogByDateBetween(LocalDate.parse(dateAfter), LocalDate.parse(dateBefore), pageable);
        }
        return logRepository.findAll(pageable);

    }
}
