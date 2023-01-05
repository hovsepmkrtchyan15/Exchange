package com.example.exchange.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ExchangeController {
    private final ExchangeService exchangeService;
    private final LogService logService;
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.000");

    @GetMapping("/exchange")
    public String exchange(ModelMap modelMap,
                           @RequestParam(value = "total", required = false) String total,
                           @RequestParam(value = "rate", required = false) String rate,
                           @RequestParam(value = "currencyNameFrom", required = false) String currencyNameFrom,
                           @RequestParam(value = "currencyNameTo", required = false) String currencyNameTo

    ) throws ParseException {
        List<ExchangeRates> exchangeRatesList = exchangeService.findAllByDate(LocalDate.now());
        modelMap.addAttribute("erl", exchangeRatesList);
        modelMap.addAttribute("total", total);
        modelMap.addAttribute("rate", rate);
        modelMap.addAttribute("currencyNameFrom", currencyNameFrom);
        modelMap.addAttribute("currencyNameTo", currencyNameTo);

        return "/exchange";
    }

    @PostMapping("/exchange/currency")
    public String change(@RequestParam(value = "idFrom", required = false) int idFrom,
                         @RequestParam(value = "idTo", required = false) int idTo,
                         @RequestParam(value = "keyword", required = false) Double sum,
                         @AuthenticationPrincipal CurrentUser currentUser,
                         RedirectAttributes redirectAttributes) {
        Double rate = exchangeService.change(idFrom, idTo);
        String rateStr = decimalFormat.format(rate);

        String currencyNameFrom = exchangeService.findById(idFrom);
        String currencyNameTo = exchangeService.findById(idTo);

        Double total = rate * sum;
        String totalStr = decimalFormat.format(total);

        redirectAttributes.addAttribute("total", totalStr);
        redirectAttributes.addAttribute("rate", rateStr);
        redirectAttributes.addAttribute("currencyNameFrom", currencyNameFrom);
        redirectAttributes.addAttribute("currencyNameTo", currencyNameTo);
        log.info("conversion done {}, from {} in {}, at the rate {}, date - {}",
                currentUser.getUsername(), currencyNameFrom, currencyNameTo, rate, LocalDate.now());
        logService.saveLog(currencyNameFrom, currencyNameTo, rate, currentUser);

        return "redirect:/exchange";
    }
}
