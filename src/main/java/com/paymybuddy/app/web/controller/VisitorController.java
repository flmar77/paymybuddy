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

import javax.persistence.EntityExistsException;

// TODO : binding result pour vérifier les formulaires
@Slf4j
@Controller
public class VisitorController {

    @Autowired
    private UserService userService;

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

    // TODO : ModelAndView ?
    @PostMapping("/visitor/createaccount")
    public ModelAndView visitorPostCreateAccount(@ModelAttribute UserModel userModel) {
        try {
            UserModel userModelCreated = userService.createUser(userModel);
            log.debug("account/[role] created : " + userModelCreated.getEmail() + '/' + userModelCreated.getRoles());
            return new ModelAndView("redirect:/visitor/createdaccount");
        } catch (EntityExistsException e) {
            log.debug("account not created because already exists : " + userModel.getEmail());
            return new ModelAndView("redirect:/visitor/notcreatedaccount");
        }
    }

    @GetMapping("/visitor/createdaccount")
    public String visitorCreatedAccount(Model model) {
        model.addAttribute("createdaccount", true);
        return "/visitor/login";
    }

    //TODO : use redirect
    @GetMapping("/visitor/notcreatedaccount")
    public String visitorNotCreatedAccount(Model model) {
        model.addAttribute("notcreatedaccount", true);
        model.addAttribute("userModel", new UserModel());
        return "/visitor/createaccount";
    }

}
