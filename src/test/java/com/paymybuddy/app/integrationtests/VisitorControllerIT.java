package com.paymybuddy.app.integrationtests;

import com.paymybuddy.app.dal.entity.UserEntity;
import com.paymybuddy.app.dal.repository.AuthorityRepository;
import com.paymybuddy.app.dal.repository.UserRepository;
import com.paymybuddy.app.domain.model.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitorControllerIT {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void should_getVisitorHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/visitor/home"))
                .andExpect(content().string(containsString("Welcome")));
    }

    @Test
    @Transactional
    public void should_createAccount() throws Exception {
        UserModel userModelToCreate = new UserModel();
        userModelToCreate.setEmail("visitorEmailIntegrationTest@integrationTest.com");
        userModelToCreate.setPassword("xxx");

        mockMvc.perform(post("/visitor/createaccount")
                        .with(csrf())
                        .flashAttr("userModel", userModelToCreate))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/login"));

        UserEntity userEntity = userRepository.findByEmail(userModelToCreate.getEmail())
                .orElse(new UserEntity());

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getEmail()).isEqualTo(userModelToCreate.getEmail());
        assertThat(userEntity.getBalance()).isEqualTo(0f);
        assertThat(userEntity.getAuthorityEntityList().get(0).getAuthority()).isEqualTo("USER");

        authorityRepository.deleteById(userEntity.getAuthorityEntityList().get(0).getId());
        userRepository.deleteById(userEntity.getId());
    }
}
