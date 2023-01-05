package com.example.exchange.repository;


import com.example.exchange.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LogRepository extends JpaRepository<Log, Integer> {

    Page<Log> findLogByDateAfter(LocalDate dateAfter, Pageable pageable);

    Page<Log> findLogByDateBefore(LocalDate dateAfter, Pageable pageable);

    Page<Log> findLogByDateBetween(LocalDate dateAfter, LocalDate dateBefore, Pageable pageable);
}
