package com.example.exchange.controller;

import com.example.exchange.dto.ExchangeDto;
import com.example.exchange.entity.ExchangeRates;
import com.example.exchange.security.CurrentUser;
import com.example.exchange.service.ExchangeService;
import com.example.exchange.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ExchangeController {
    private final ExchangeService exchangeService;
    private final LogService logService;


    @GetMapping("/exchange")
    public String exchange(ModelMap modelMap,
                           @ModelAttribute ExchangeDto exchangeDto) {
        List<ExchangeRates> exchangeRatesList = exchangeService.findAllByDate(LocalDate.now());
        modelMap.addAttribute("erl", exchangeRatesList);
        modelMap.addAttribute("total", exchangeDto);
        return "/exchange";
    }

    @PostMapping("/exchange/currency")
    public String change(@RequestParam(value = "idFrom", required = false) int idFrom,
                         @RequestParam(value = "idTo", required = false) int idTo,
                         @RequestParam(value = "keyword", required = false) BigDecimal sum,
                         @AuthenticationPrincipal CurrentUser currentUser,
                         RedirectAttributes redirectAttributes) {
        ExchangeDto exchangeDto = exchangeService.total(idFrom, idTo, sum);
        redirectAttributes.addFlashAttribute(exchangeDto);
        log.info("conversion done {}, from {} in {}, at the rate {}, date - {}",
                currentUser.getUsername(), exchangeDto.getCurrencyNameFrom(), exchangeDto.getCurrencyNameTo(),
                exchangeDto.getRate(), LocalDate.now());
        logService.saveLog(exchangeDto.getCurrencyNameFrom(), exchangeDto.getCurrencyNameTo(), exchangeDto.getRate(), currentUser);

        return "redirect:/exchange";
    }

}
