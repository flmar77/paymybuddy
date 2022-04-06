package com.paymybuddy.app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pmbController {

    @GetMapping("/")
    public String home() {
        return "homesignin";
    }

    // Login form
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    // Login form with error
    @RequestMapping("/loginerror")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
