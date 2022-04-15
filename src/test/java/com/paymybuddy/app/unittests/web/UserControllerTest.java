package com.paymybuddy.app.unittests.web;

import com.paymybuddy.app.dal.entity.ConnectionEntity;
import com.paymybuddy.app.domain.helper.ConnectionHelper;
import com.paymybuddy.app.domain.model.InTransactionModel;
import com.paymybuddy.app.domain.model.OutTransactionModel;
import com.paymybuddy.app.domain.model.UserModel;
import com.paymybuddy.app.domain.service.InTransactionService;
import com.paymybuddy.app.domain.service.OutTransactionService;
import com.paymybuddy.app.domain.service.UserService;
import com.paymybuddy.app.web.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private InTransactionService inTransactionService;

    @MockBean
    private OutTransactionService outTransactionService;

    @MockBean
    private ConnectionHelper connectionHelper;

    @Test
    @WithMockUser
    public void should_returnUserHome_whenGetUserHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/home"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    @WithMockUser
    public void should_returnUserTransferIn_whenGetUserTransferIn() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(getFakeUserModel());

        mockMvc.perform(get("/user/transferin"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/transferin"))
                .andExpect(content().string(containsString("yyy@mail.com")));
    }

    @Test
    @WithMockUser
    public void should_redirectUserTransferInWithCustomFlashMissing_whenPostMissingUserTransferIn() throws Exception {
        when(inTransactionService.createInTransaction(any())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/user/transferin")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/transferin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("inTransactionNotDoneForMissingConnectedEmail", true));
    }

    @Test
    @WithMockUser
    public void should_redirectUserTransferInWithCustomFlashUnsupported_whenPostUserUnsupportedTransferIn() throws Exception {
        when(inTransactionService.createInTransaction(any())).thenThrow(UnsupportedOperationException.class);

        mockMvc.perform(post("/user/transferin")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/transferin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("inTransactionNotDoneForInsufficientBalance", true));
    }

    @Test
    @WithMockUser
    public void should_redirectUserTransferInWithCustomFlashDone_whenPostValidUserTransferIn() throws Exception {
        when(inTransactionService.createInTransaction(any())).thenReturn(getFakeInTransactionModel());

        mockMvc.perform(post("/user/transferin")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/transferin"))
                .andExpect(MockMvcResultMatchers.flash().attribute("inTransactionDone", true));
    }

    @Test
    @WithMockUser
    public void should_returnUserConnection_whenGetUserConnection() throws Exception {
        when(userService.getUserAvailableEmailsByEmail(anyString())).thenReturn(Arrays.asList("yyy@mail.com", "zzz@mail.com"));
        when(userService.getUserByEmail(anyString())).thenReturn(getFakeUserModel());

        mockMvc.perform(get("/user/connection"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/connection"))
                .andExpect(content().string(containsString("yyy@mail.com")));
    }

    @Test
    @WithMockUser
    public void should_redirectUserConnectionWithCustomFlashMissing_whenPostMissingConnection() throws Exception {
        when(connectionHelper.CreateConnectionFromEmails(anyString(), any())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(post("/user/connection")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/connection"))
                .andExpect(MockMvcResultMatchers.flash().attribute("connectionNotDone", true));
    }

    @Test
    @WithMockUser
    public void should_redirectUserConnectionWithCustomFlashDone_whenPostValidConnection() throws Exception {
        when(connectionHelper.CreateConnectionFromEmails(anyString(), any())).thenReturn(getFakeConnectionEntity());

        mockMvc.perform(post("/user/connection")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/connection"))
                .andExpect(MockMvcResultMatchers.flash().attribute("connectionDone", true));
    }

    @Test
    @WithMockUser
    public void should_returnUserTransferOut_whenGetUserTransferOut() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(getFakeUserModel());

        mockMvc.perform(get("/user/transferout"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/transferout"))
                .andExpect(content().string(containsString("iban")));
    }

    @Test
    @WithMockUser
    public void should_redirectUserTransferOutWithCustomFlashUnsupported_whenPostUserUnsupportedTransferOut() throws Exception {
        when(outTransactionService.createOutTransaction(any())).thenThrow(UnsupportedOperationException.class);

        mockMvc.perform(post("/user/transferout")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/transferout"))
                .andExpect(MockMvcResultMatchers.flash().attribute("outTransactionNotDone", true));
    }

    @Test
    @WithMockUser
    public void should_redirectUserTransferOutWithCustomFlashDone_whenPostValidUserTransferOut() throws Exception {
        when(outTransactionService.createOutTransaction(any())).thenReturn(getFakeOutTransactionModel());

        mockMvc.perform(post("/user/transferout")
                        .with(csrf()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/transferout"))
                .andExpect(MockMvcResultMatchers.flash().attribute("outTransactionDone", true));
    }

    @Test
    @WithMockUser
    public void should_returnUserProfile_whenGetUserProfile() throws Exception {
        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/profile"));
    }

    @Test
    @WithMockUser
    public void should_returnUserContact_whenGetUserContact() throws Exception {
        mockMvc.perform(get("/user/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/contact"));
    }

    private UserModel getFakeUserModel() {
        UserModel userModel = new UserModel();
        userModel.setEmail("xxx@mail.com");
        userModel.setInTransactionModelList(Collections.singletonList(getFakeInTransactionModel()));
        userModel.setOutTransactionModelList(Collections.singletonList(getFakeOutTransactionModel()));
        return userModel;
    }

    private InTransactionModel getFakeInTransactionModel() {
        InTransactionModel inTransactionModel = new InTransactionModel();
        inTransactionModel.setId(1);
        inTransactionModel.setDescription("desc");
        inTransactionModel.setMonetizedAmount(0.1f);
        inTransactionModel.setGivenAmount(20f);
        inTransactionModel.setConnectorEmail("xxx@mail.com");
        inTransactionModel.setConnectedEmail("yyy@mail.com");
        return inTransactionModel;
    }

    private ConnectionEntity getFakeConnectionEntity() {
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setId(1);
        connectionEntity.setConnectorId(1);
        connectionEntity.setConnectedId(2);
        return connectionEntity;
    }

    private OutTransactionModel getFakeOutTransactionModel() {
        OutTransactionModel outTransactionModel = new OutTransactionModel();
        outTransactionModel.setId(1);
        outTransactionModel.setDescription("desc");
        outTransactionModel.setMonetizedAmount(0.1f);
        outTransactionModel.setTransferredAmount(20f);
        outTransactionModel.setIban("iban");
        outTransactionModel.setUserEmail("xxx@mail.com");
        return outTransactionModel;
    }
}