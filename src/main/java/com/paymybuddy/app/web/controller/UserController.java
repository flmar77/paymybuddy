package com.paymybuddy.app.web.controller;

import com.paymybuddy.app.dal.entity.ConnectionEntity;
import com.paymybuddy.app.domain.model.InTransactionModel;
import com.paymybuddy.app.domain.model.UserModel;
import com.paymybuddy.app.domain.service.ConnectionService;
import com.paymybuddy.app.domain.service.InTransactionService;
import com.paymybuddy.app.domain.service.UserService;
import com.paymybuddy.app.web.dto.StringFormDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// TODO : binding result pour vérifier les formulaires
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InTransactionService inTransactionService;

    @Autowired
    private ConnectionService connectionService;

    @GetMapping("/user/home")
    public String userHome(Authentication authentication,
                           Model model) {
        model.addAttribute("username", authentication.getName());
        return "/user/home";
    }

    @GetMapping("/user/transferin")
    public String userGetTransferIn(Authentication authentication,
                                    Model model) {
        UserModel userModel = userService.getUserByEmail(authentication.getName());
        model.addAttribute("inTransactionModel", new InTransactionModel());
        model.addAttribute("connectedEmails", userModel.getConnectedEmails());
        model.addAttribute("inTransactionModelList", userModel.getInTransactionModelList());
        return "/user/transferin";
    }

    @PostMapping("/user/transferin")
    public String userPostTransferIn(Authentication authentication,
                                     @ModelAttribute InTransactionModel inTransactionModelToSave,
                                     RedirectAttributes redirectAttributes) {
        try {
            inTransactionModelToSave.setConnectorEmail(authentication.getName());
            InTransactionModel inTransactionModelSaved = inTransactionService.createInTransaction(inTransactionModelToSave);
            log.debug("inTransaction created with id : " + inTransactionModelSaved.getId());
            redirectAttributes.addFlashAttribute("inTransactionDone", true);
            return "redirect:/user/transferin";
        } catch (UnsupportedOperationException e) {
            log.debug("inTransaction not done because of insufficient balance");
            redirectAttributes.addFlashAttribute("inTransactionNotDone", true);
            return "redirect:/user/transferin";
        }

    }

    @GetMapping("/user/connection")
    public String userGetConnection(Authentication authentication,
                                    Model model) {
        model.addAttribute("stringFormDto", new StringFormDto());
        List<String> availableEmails = userService.getUserAvailableEmailsByEmail(authentication.getName());
        model.addAttribute("availableEmails", availableEmails);

        UserModel userModel = userService.getUserByEmail(authentication.getName());
        model.addAttribute("connectedEmails", userModel.getConnectedEmails());

        return "/user/connection";
    }

    @PostMapping("/user/connection")
    public String userPostConnection(Authentication authentication,
                                     @ModelAttribute StringFormDto stringFormDto,
                                     RedirectAttributes redirectAttributes) {
        ConnectionEntity connectionEntitySaved = connectionService.CreateConnectionFromEmails(authentication.getName(), stringFormDto.getText());
        log.debug("connection created for connector/connectedIds : " + connectionEntitySaved.getConnectorId() + "/" + connectionEntitySaved.getConnectedId());
        redirectAttributes.addFlashAttribute("connectionDone", true);
        return "redirect:/user/connection";
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
