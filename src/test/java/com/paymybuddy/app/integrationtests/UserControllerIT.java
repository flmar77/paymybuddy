package com.paymybuddy.app.integrationtests;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import com.paymybuddy.app.dal.entity.UserEntity;
import com.paymybuddy.app.dal.repository.AuthorityRepository;
import com.paymybuddy.app.dal.repository.OutTransactionRepository;
import com.paymybuddy.app.dal.repository.UserRepository;
import com.paymybuddy.app.domain.model.OutTransactionModel;
import com.paymybuddy.app.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private OutTransactionRepository outTransactionRepository;

    @Autowired
    private UserService userService;

    private final static String USER_EMAIL = "userEmailIntegrationTest@integrationTest.com";

    @Test
    @WithMockUser
    public void should_getUserHome() throws Exception {
        mockMvc.perform(get("/user/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/home"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    @WithMockUser(username = USER_EMAIL)
    public void should_saveOutTransaction() throws Exception {
        UserEntity userEntitySaved = saveTestUser();
        AuthorityEntity authorityEntitySaved = saveTestAuthority(userEntitySaved.getId());

        OutTransactionModel outTransactionModelToSave = getFakeOutTransactionModel();

        mockMvc.perform(post("/user/transferout")
                        .with(csrf())
                        .flashAttr("outTransactionModel", outTransactionModelToSave))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/user/transferout"))
                .andExpect(MockMvcResultMatchers.flash().attribute("outTransactionDone", true));

        List<OutTransactionEntity> outTransactionEntityList = outTransactionRepository.findByUserId(userEntitySaved.getId());
        UserEntity userEntityRefreshed = userRepository.findById(userEntitySaved.getId())
                .orElse(new UserEntity());
        assertThat(userEntityRefreshed).isNotNull();
        assertThat(userEntityRefreshed.getBalance()).isNotEqualTo(0f);
        assertThat(outTransactionEntityList.get(0).getIban()).isEqualTo("iban");

        outTransactionRepository.deleteById(outTransactionEntityList.get(0).getId());
        authorityRepository.deleteById(authorityEntitySaved.getId());
        userRepository.deleteById(userEntitySaved.getId());
    }

    private UserEntity saveTestUser() {
        UserEntity userEntityToSave = new UserEntity();
        userEntityToSave.setEmail(USER_EMAIL);
        userEntityToSave.setPassword(passwordEncoder.encode("xxx"));
        userEntityToSave.setEnabled(true);
        userEntityToSave.setBalance(0f);
        return userRepository.save(userEntityToSave);
    }

    private AuthorityEntity saveTestAuthority(Integer userId) {
        AuthorityEntity authorityEntityToSave = new AuthorityEntity();
        authorityEntityToSave.setUserId(userId);
        authorityEntityToSave.setAuthority("USER");
        return authorityRepository.save(authorityEntityToSave);
    }

    private OutTransactionModel getFakeOutTransactionModel() {
        OutTransactionModel outTransactionModel = new OutTransactionModel();
        outTransactionModel.setDescription("desc");
        outTransactionModel.setTransferredAmount(20f);
        outTransactionModel.setIban("iban");
        return outTransactionModel;
    }

}
