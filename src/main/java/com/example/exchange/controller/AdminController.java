package com.example.exchange.controller;

import com.example.exchange.entity.Log;
import com.example.exchange.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final LogService logService;

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin";
    }

    @GetMapping("/history")
    public String history(@PageableDefault(size = 3) Pageable pageable,
                          @RequestParam(value = "dateAfter", required = false) String dateAfter,
                          @RequestParam(value = "dateBefore", required = false) String dateBefore,
                          ModelMap modelMap) {
        Page<Log> listLog = logService.findByDate(dateAfter, dateBefore, pageable);
        int totalPages = listLog.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        modelMap.addAttribute("listLog", listLog);
        return "/logPage";
    }
}
