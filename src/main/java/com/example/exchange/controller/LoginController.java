package com.example.exchange.controller;


import com.example.exchange.entity.RoleUser;
import com.example.exchange.entity.User;
import com.example.exchange.security.CurrentUser;
import com.example.exchange.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping(value = "/")
    public String loginPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        if (error != null && error.equals("true"))
            modelMap.addAttribute("error", "true");
        return "/loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           ModelMap modelMap) {
        Optional<User> byEmail = loginService.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            modelMap.addAttribute("errorMessage", "email already in use");
            return "redirect:/";
        }
        loginService.saveUser(user);
        return "loginPage";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "/accessDenied";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser != null) {
            User user = currentUser.getUser();
            if (user.getRoleUser() == RoleUser.ADMIN) {
                return "redirect:/admin";
            }
        }
        return "redirect:/exchange";
    }
}
