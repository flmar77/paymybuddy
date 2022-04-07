package com.paymybuddy.app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class pmbController {

    @GetMapping("/")
    public String visitorHome() {
        return "/visitor/home";
    }

    // Login form
    @RequestMapping("/login")
    public String visitorLogin() {
        return "/visitor/login";
    }

    // Login form with error
    @RequestMapping("/loginerror")
    public String visitorLoginError(Model model) {
        model.addAttribute("loginError", true);
        return "/visitor/login";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "/user/home";
    }

    @GetMapping("/user/transferin")
    public String userTransferIn() {
        return "/user/transferin";
    }

    @GetMapping("/user/transferout")
    public String userTransferOut() {
        return "/user/transferout";
    }

    @GetMapping("/user/profile")
    public String userProfile() {
        return "/user/profile";
    }

    @GetMapping("/user/contact")
    public String userContact() {
        return "user/contact";
    }

}
