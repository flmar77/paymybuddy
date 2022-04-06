package com.paymybuddy.app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class pmbController {

    @GetMapping("/")
    public String home() {
        return "homeSignIn";
    }
}
