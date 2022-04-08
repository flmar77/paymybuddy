package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.entity.InTransactionEntity;
import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import com.paymybuddy.app.dal.entity.UserEntity;
import com.paymybuddy.app.dal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found : " + username));

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (AuthorityEntity authorityEntity : userEntity.getAuthorityEntityList()) {
            authorities.add(new SimpleGrantedAuthority(authorityEntity.getAuthority()));
        }

        return new org.springframework.security.core.userdetails.User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities);
    }

    @Transactional
    public List<UserEntity> getAllUsers() {
        List<UserEntity> userEntityList = (List<UserEntity>) userRepository.findAll();
        for (UserEntity userEntity : userEntityList) {
            String message = userEntity.getId() + " "
                    + userEntity.getEmail() + " "
                    + userEntity.getBalance();
            for (UserEntity connectedUser : userEntity.getConnectedUsers()) {
                message = message + "/" + connectedUser.getEmail();
            }
            for (InTransactionEntity inTransactionEntity : userEntity.getInTransactionEntityList()) {
                message = message + "-" + inTransactionEntity.getDescription();
            }
            for (OutTransactionEntity outTransactionEntity : userEntity.getOutTransactionEntityList()) {
                message = message + "_" + outTransactionEntity.getDescription();
            }
            for (AuthorityEntity authorityEntity : userEntity.getAuthorityEntityList()) {
                message = message + "|" + authorityEntity.getAuthority();
            }
            log.debug(message);
        }
        return userEntityList;
    }

}
