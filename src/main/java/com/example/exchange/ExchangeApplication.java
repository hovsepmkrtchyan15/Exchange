package com.example.exchange;

import com.example.exchange.entity.ExchangeRates;
import com.example.exchange.entity.RoleUser;
import com.example.exchange.entity.User;
import com.example.exchange.repository.ExchangeRatesRepository;
import com.example.exchange.repository.UserRepository;
import com.example.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@RequiredArgsConstructor
public class ExchangeApplication implements CommandLineRunner {

    private final ExchangeService exchangeService;
    private final UserRepository userRepository;
    private final ExchangeRatesRepository exchangeRatesRepository;


    public static void main(String[] args) {
        SpringApplication.run(ExchangeApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
        Optional<List<ExchangeRates>> allByDate = exchangeRatesRepository.findAllByDate(LocalDate.now());
        if (allByDate.get().size() == 0) {
            exchangeService.saveRates();
        }

        Optional<User> byEmail = userRepository.findByEmail("admin@gmail.com");
        if (byEmail.isEmpty()) {
            userRepository.save(User.builder()
                    .email("admin@gmail.com")
                    .password(passwordEncoder().encode("admin"))
                    .roleUser(RoleUser.ADMIN)
                    .build());
        }
    }
}
