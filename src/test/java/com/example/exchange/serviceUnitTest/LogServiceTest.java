package com.example.exchange.serviceUnitTest;

import com.example.exchange.entity.Log;
import com.example.exchange.entity.RoleUser;
import com.example.exchange.entity.User;
import com.example.exchange.repository.LogRepository;
import com.example.exchange.security.CurrentUser;
import com.example.exchange.service.LogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class LogServiceTest {
    @MockBean
    private LogRepository logRepository;

    @Autowired
    private LogService logService;

    @Test
    void saveLog() {
        // Create test data
        CurrentUser currentUser = new CurrentUser(User.builder()
                .password("test")
                .email("test@test.ru")
                .roleUser(RoleUser.ADMIN)
                .build());


        Log logDB = Log.builder()
                .user(currentUser.getUser())
                .rate(new BigDecimal(5))
                .currencyNameFrom("use")
                .currencyNameTo("eur")
                .date(LocalDate.now())
                .build();
        logService.saveLog("use", "eur", new BigDecimal(5), currentUser);

        verify(logRepository, times(1)).save(logDB);
    }

    @Test
    public void testFindByDateAfter() {

// Create test data
        Pageable pageable = PageRequest.of(0, 10);
        CurrentUser currentUser = new CurrentUser(User.builder()
                .password("test")
                .email("test@test.ru")
                .roleUser(RoleUser.ADMIN)
                .build());

        Log log1 = new Log(0, currentUser.getUser(), LocalDate.of(2023, 1, 1), "use", "eur", new BigDecimal(2));
        Log log2 = new Log(1, currentUser.getUser(), LocalDate.of(2023, 2, 2), "use", "eur", new BigDecimal(2));
        Log log3 = new Log(2, currentUser.getUser(), LocalDate.of(2023, 3, 3), "use", "eur", new BigDecimal(2));
        List<Log> logAfter = Arrays.asList(log2, log3);

        when(logRepository.findLogByDateAfter(LocalDate.of(2023, 1, 1), pageable)).thenReturn(new PageImpl<>(logAfter));

        // Test find by date after
        Page<Log> after = logService.findByDate("2023-01-01", "", pageable);
        assertEquals(2, after.getTotalElements());
        assertEquals(1, logAfter.get(0).getId());
        assertEquals(2, logAfter.get(1).getId());
        verify(logRepository, times(1)).findLogByDateAfter(LocalDate.of(2023, 1, 1), pageable);
    }

    @Test
    public void testFindByDateBefore() {
       // Create test data
        Pageable pageable = PageRequest.of(0, 10);
        CurrentUser currentUser = new CurrentUser(User.builder()
                .password("test")
                .email("test@test.ru")
                .roleUser(RoleUser.ADMIN)
                .build());

        Log log1 = new Log(0, currentUser.getUser(), LocalDate.of(2023, 1, 1), "use", "eur", new BigDecimal(2));
        Log log2 = new Log(1, currentUser.getUser(), LocalDate.of(2023, 2, 2), "use", "eur", new BigDecimal(2));
        Log log3 = new Log(2, currentUser.getUser(), LocalDate.of(2023, 3, 3), "use", "eur", new BigDecimal(2));
        List<Log> logBefore = Arrays.asList(log1, log2);

        when(logRepository.findLogByDateBefore(LocalDate.of(2023, 3, 3), pageable)).thenReturn(new PageImpl<>(logBefore));

        // Test find by date before
        Page<Log> before = logService.findByDate("", "2023-03-03", pageable);
        assertEquals(2, before.getTotalElements());
        assertEquals(0, logBefore.get(0).getId());
        assertEquals(1, logBefore.get(1).getId());
        verify(logRepository, times(1)).findLogByDateBefore(LocalDate.of(2023, 3, 3), pageable);
    }

    @Test
    public void testFindByDateBetween() {
        // Create test data
        Pageable pageable = PageRequest.of(0, 10);
        CurrentUser currentUser = new CurrentUser(User.builder()
                .password("test")
                .email("test@test.ru")
                .roleUser(RoleUser.ADMIN)
                .build());

        Log log1 = new Log(0, currentUser.getUser(), LocalDate.of(2023, 1, 1), "use", "eur", new BigDecimal(2));
        Log log2 = new Log(1, currentUser.getUser(), LocalDate.of(2023, 2, 2), "use", "eur", new BigDecimal(2));
        Log log3 = new Log(2, currentUser.getUser(), LocalDate.of(2023, 3, 3), "use", "eur", new BigDecimal(2));
        List<Log> logBetween = List.of(log2);

        when(logRepository.findLogByDateBetween(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 3, 3), pageable)).thenReturn(new PageImpl<>(logBetween));

        // Test find by date between
        Page<Log> between = logService.findByDate("2023-01-01", "2023-03-03", pageable);
        assertEquals(1, between.getTotalElements());
        assertEquals(1, logBetween.get(0).getId());
        verify(logRepository, times(1)).findLogByDateBetween(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 3, 3), pageable);
    }

    @Test
    public void testFindAll() {
        // Create test data
        Pageable pageable = PageRequest.of(0, 10);
        CurrentUser currentUser = new CurrentUser(User.builder()
                .password("test")
                .email("test@test.ru")
                .roleUser(RoleUser.ADMIN)
                .build());

        Log log1 = new Log(0, currentUser.getUser(), LocalDate.of(2023, 1, 1), "use", "eur", new BigDecimal(2));
        Log log2 = new Log(1, currentUser.getUser(), LocalDate.of(2023, 2, 2), "use", "eur", new BigDecimal(2));
        Log log3 = new Log(2, currentUser.getUser(), LocalDate.of(2023, 3, 3), "use", "eur", new BigDecimal(2));
        List<Log> logAll = Arrays.asList(log1, log2, log3);

        when(logRepository.findAll(pageable)).thenReturn(new PageImpl<>(logAll));

        // Test find all
        Page<Log> all = logService.findByDate("", "", pageable);
        assertEquals(3, all.getTotalElements());
        verify(logRepository, times(1)).findAll(pageable);

    }

}
