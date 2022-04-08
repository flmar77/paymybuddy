package com.paymybuddy.app.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO : binding result pour vérifier les formulaires

@Controller
public class pmbController {

    // visitor
    @GetMapping("/")
    public String visitorHome() {
        return "/visitor/home";
    }

    @RequestMapping("/login")
    public String visitorLogin() {
        return "/visitor/login";
    }

    @RequestMapping("/loginerror")
    public String visitorLoginError(Model model) {
        model.addAttribute("loginError", true);
        return "/visitor/login";
    }

    @RequestMapping("/loginlogout")
    public String visitorLoginLogout(Model model) {
        model.addAttribute("loginLogout", true);
        return "/visitor/login";
    }

    @RequestMapping("/visitor/createaccount")
    public String visitorCreateAccount() {
        return "/visitor/createaccount";
    }

    @RequestMapping("/visitor/createdaccount")
    public String visitorCreatedAccount(Model model) {
        model.addAttribute("createdaccount", true);
        return "/visitor/login";
    }

    // user
    @GetMapping("/user/home")
    public String userHome() {
        return "/user/home";
    }

    @GetMapping("/user/transferin")
    public String userTransferIn() {
        return "/user/transferin";
    }

    @GetMapping("/user/connection")
    public String userConnection() {
        return "/user/connection";
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
