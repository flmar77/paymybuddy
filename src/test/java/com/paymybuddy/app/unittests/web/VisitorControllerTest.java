package com.paymybuddy.app.unittests.web;

import com.paymybuddy.app.domain.model.UserModel;
import com.paymybuddy.app.domain.service.UserService;
import com.paymybuddy.app.web.controller.VisitorController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityExistsException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VisitorController.class)
public class VisitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_returnVisitorHome_whenGetRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/visitor/home"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    public void should_returnVisitorLogin_whenGetLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/visitor/login"))
                .andExpect(content().string(containsString("Login")));
    }

    @Test
    public void should_redirectLogin_whenGetLoginError() throws Exception {
        mockMvc.perform(get("/loginerror"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void should_redirectLogin_whenGetLoginLogout() throws Exception {
        mockMvc.perform(get("/loginlogout"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void should_returnVisitorCreateAccount_whenGetVisitorCreateAccount() throws Exception {
        mockMvc.perform(get("/visitor/createaccount"))
                .andExpect(status().isOk())
                .andExpect(view().name("/visitor/createaccount"))
                .andExpect(content().string(containsString("account")));
    }

    @Test
    public void should_redirectLogin_whenPostVisitorCreateValidAccount() throws Exception {
        when(userService.createUser(any())).thenReturn(new UserModel());

        mockMvc.perform(post("/visitor/createaccount")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    public void should_redirectVisitorCreateAccount_whenPostVisitorCreateExistingAccount() throws Exception {
        when(userService.createUser(any())).thenThrow(EntityExistsException.class);

        mockMvc.perform(post("/visitor/createaccount")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/visitor/createaccount"));
    }

}
