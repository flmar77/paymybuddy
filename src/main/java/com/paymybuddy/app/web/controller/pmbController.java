package com.paymybuddy.app.web.controller;

import com.paymybuddy.app.domain.model.UserModel;
import com.paymybuddy.app.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

// TODO : binding result pour vérifier les formulaires
@Slf4j
@Controller
public class pmbController {

    @Autowired
    private UserService userService;

    // visitor
    @GetMapping("/")
    public String visitorHome() {
        return "/visitor/home";
    }

    @GetMapping("/login")
    public String visitorLogin() {
        return "/visitor/login";
    }

    @GetMapping("/loginerror")
    public String visitorLoginError(Model model) {
        model.addAttribute("loginError", true);
        return "/visitor/login";
    }

    @GetMapping("/loginlogout")
    public String visitorLoginLogout(Model model) {
        model.addAttribute("loginLogout", true);
        return "/visitor/login";
    }

    @GetMapping("/visitor/createaccount")
    public String visitorGetCreateAccount(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "/visitor/createaccount";
    }

    @PostMapping("/visitor/createaccount")
    public ModelAndView visitorPostCreateAccount(@ModelAttribute UserModel userModel) {
        log.debug(userModel.getEmail() + " " + userModel.getPassword());
        UserModel userModel1 = userService.createUser(userModel);
        log.debug(userModel1.getEmail());
        return new ModelAndView("redirect:/visitor/createdaccount");
    }


    @GetMapping("/visitor/createdaccount")
    public String visitorPostCreateAccount(Model model) {
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
