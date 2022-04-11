package com.paymybuddy.app.web.controller;

import com.paymybuddy.app.domain.model.InTransactionModel;
import com.paymybuddy.app.domain.model.UserModel;
import com.paymybuddy.app.domain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

// TODO : binding result pour vérifier les formulaires
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/home")
    public String userHome(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "/user/home";
    }

    @GetMapping("/user/transferin")
    public String userTransferIn(Authentication authentication, Model model) {
        UserModel userModel = userService.getUserByEmail(authentication.getName());
        List<InTransactionModel> inTransactionModelList = userModel.getInTransactionModelList();
        model.addAttribute("inTransactionModelList", inTransactionModelList);
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
