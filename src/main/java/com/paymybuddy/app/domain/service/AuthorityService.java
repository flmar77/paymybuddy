package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public AuthorityEntity createAuthority(Integer userId) {

        AuthorityEntity authorityEntityToSave = new AuthorityEntity();
        authorityEntityToSave.setUserId(userId);
        authorityEntityToSave.setAuthority("USER");

        return authorityRepository.save(authorityEntityToSave);

    }


}
