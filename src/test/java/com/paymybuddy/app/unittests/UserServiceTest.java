package com.paymybuddy.app.unittests;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.entity.UserEntity;
import com.paymybuddy.app.dal.repository.UserRepository;
import com.paymybuddy.app.domain.model.UserModel;
import com.paymybuddy.app.domain.service.AuthorityService;
import com.paymybuddy.app.domain.service.InTransactionService;
import com.paymybuddy.app.domain.service.OutTransactionService;
import com.paymybuddy.app.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityExistsException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorityService authorityService;

    @Mock
    private InTransactionService inTransactionService;

    @Mock
    private OutTransactionService outTransactionService;

    @Test
    public void should_throwUsernameNotFoundException_whenLoadMissingUserByUsername() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> userService.loadUserByUsername(anyString()));
    }

    @Test
    public void should_returnSomething_whenLoadExistingUserByUsername() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.loadUserByUsername(anyString())).isNotNull();
    }

    @Test
    public void should_throwEntityExistsException_whenCreateExistingUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThatExceptionOfType(EntityExistsException.class)
                .isThrownBy(() -> userService.createUser(getFakeUserModel()));
    }

    @Test
    public void should_returnSomething_whenCreateNewUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("xxx");
        when(userRepository.save(any())).thenReturn(getFakeUserEntity());
        when(authorityService.createAuthority(any())).thenReturn(getFakeAuthority());

        assertThat(userService.createUser(getFakeUserModel())).isNotNull();
    }

    @Test
    public void should_throwNoSuchElementException_whenGetMissingUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.getUserByEmail(anyString()));
    }

    @Test
    public void should_returnSomething_whenGetExistingUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.getUserByEmail(anyString())).isNotNull();
    }

    @Test
    public void should_throwNoSuchElementException_whenGetMissingUserEmailById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.getUserEmailById(anyInt()));
    }

    @Test
    public void should_returnEmail_whenGetExistingUserEmailById() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.getUserEmailById(anyInt())).isEqualTo("xxx@mail.com");
    }

    @Test
    public void should_throwNoSuchElementException_whenGetMissingUserIdByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.getUserIdByEmail(anyString()));
    }

    @Test
    public void should_returnId_whenGetExistingUserIdByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.getUserIdByEmail(anyString())).isEqualTo(1);
    }

    @Test
    public void should_throwNoSuchElementException_whenGetMissingUserBalanceByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.getUserBalanceByEmail(anyString()));
    }

    @Test
    public void should_returnBalance_whenGetExistingUserBalanceByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.getUserBalanceByEmail(anyString())).isEqualTo(500);
    }

    @Test
    public void should_throwNoSuchElementException_whenUpdateMissingUserBalanceByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> userService.updateUserBalanceByEmail(anyString(), 1));
    }

    @Test
    public void should_saveUser_whenUpdateExistingUserBalanceByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));
        when(userRepository.save(any(UserEntity.class))).thenReturn(getFakeUserEntity());

        userService.updateUserBalanceByEmail(anyString(), 1);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void should_returnAvailableEmails_whenGetUserAvailableEmailsByEmail() {
        when(userRepository.findAllEmails()).thenReturn(Arrays.asList("xxx@mail.com", "yyy@mail.com"));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getFakeUserEntity()));

        assertThat(userService.getUserAvailableEmailsByEmail("xxx@mail.com")).isEqualTo(Collections.singletonList("yyy@mail.com"));
    }
    
    private UserEntity getFakeUserEntity() {
        UserEntity userEntity = new UserEntity();
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority("USER");
        userEntity.setAuthorityEntityList(Collections.singletonList(authorityEntity));
        userEntity.setEmail("xxx@mail.com");
        userEntity.setPassword("xxx");
        userEntity.setId(1);
        userEntity.setBalance(500);
        userEntity.setConnectedUsers(new ArrayList<>());
        return userEntity;
    }

    private UserModel getFakeUserModel() {
        UserModel userModel = new UserModel();
        userModel.setEmail("xxx@mail.com");
        userModel.setPassword("xxx");
        return userModel;
    }

    private AuthorityEntity getFakeAuthority() {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setId(1);
        authorityEntity.setUserId(1);
        authorityEntity.setAuthority("USER");
        return authorityEntity;
    }

}
