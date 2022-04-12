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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String visitorLoginError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginError", true);
        return "redirect:/login";
    }

    @GetMapping("/loginlogout")
    public String visitorLoginLogout(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginLogout", true);
        return "redirect:/login";
    }

    @GetMapping("/visitor/createaccount")
    public String visitorGetCreateAccount(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "/visitor/createaccount";
    }

    @PostMapping("/visitor/createaccount")
    public String visitorPostCreateAccount(@ModelAttribute UserModel userModel,
                                           RedirectAttributes redirectAttributes) {
        try {
            UserModel userModelCreated = userService.createUser(userModel);
            log.debug("account/[role] created : " + userModelCreated.getEmail() + '/' + userModelCreated.getRoles());
            redirectAttributes.addFlashAttribute("createdaccount", true);
            return "redirect:/login";
        } catch (EntityExistsException e) {
            log.debug("account not created because already exists : " + userModel.getEmail());
            redirectAttributes.addFlashAttribute("notcreatedaccount", true);
            return "redirect:/visitor/createaccount";
        }
    }

}
