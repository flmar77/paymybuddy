package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import com.paymybuddy.app.dal.entity.UserEntity;
import com.paymybuddy.app.dal.repository.UserRepository;
import com.paymybuddy.app.domain.model.OutTransactionModel;
import com.paymybuddy.app.domain.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InTransactionService inTransactionService;

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
    public UserModel createUser(UserModel userModel) throws EntityExistsException {
        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            throw new EntityExistsException();
        }
        UserEntity userEntityToSave = new UserEntity();
        userEntityToSave.setEmail(userModel.getEmail());
        userEntityToSave.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userEntityToSave.setEnabled(true);
        UserEntity userEntitySaved = userRepository.save(userEntityToSave);

        AuthorityEntity authorityEntitySaved = authorityService.createAuthority(userEntitySaved.getId());
        userEntitySaved.setAuthorityEntityList(Collections.singletonList(authorityEntitySaved));

        return mapUserEntityToUserModel(userEntitySaved);
    }

    public UserModel getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        return mapUserEntityToUserModel(userEntity);
    }

    public String getUserEmailById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(NoSuchElementException::new);

        return userEntity.getEmail();
    }

    public Integer getUserIdByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        return userEntity.getId();
    }

    public float getUserBalanceByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        return userEntity.getBalance();
    }

    public void updateUserBalanceByEmail(String email, float amount) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);

        userEntity.setBalance(userEntity.getBalance() + amount);
        userRepository.save(userEntity);
    }

    private UserModel mapUserEntityToUserModel(UserEntity userEntity) {
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId());
        userModel.setEmail(userEntity.getEmail());
        userModel.setPassword(userEntity.getPassword());
        userModel.setConnectedEmails(userEntity.getConnectedUsers().stream()
                .map(UserEntity::getEmail)
                .collect(Collectors.toList()));
        userModel.setInTransactionModelList(inTransactionService.mapInTransactionEntityListToInTransactionModelList(userEntity.getInTransactionEntityList()));
        userModel.setOutTransactionModelList(mapOutTransactionEntityToOuTransactionModel(userEntity.getOutTransactionEntityList()));
        userModel.setRoles(userEntity.getAuthorityEntityList().stream()
                .map(AuthorityEntity::getAuthority)
                .collect(Collectors.toList()));
        return userModel;
    }

    // TODO : move to outTrans service
    private List<OutTransactionModel> mapOutTransactionEntityToOuTransactionModel(List<OutTransactionEntity> outTransactionEntityList) {
        if (outTransactionEntityList == null) {
            return null;
        }
        return outTransactionEntityList.stream()
                .map(outTransactionEntity -> {
                    OutTransactionModel outTransactionModel = new OutTransactionModel();
                    outTransactionModel.setId(outTransactionEntity.getId());
                    outTransactionModel.setDescription(outTransactionEntity.getDescription());
                    outTransactionModel.setMonetizedAmount(outTransactionEntity.getMonetizedAmount());
                    outTransactionModel.setTransferredAmount(outTransactionEntity.getTransferredAmount());
                    outTransactionModel.setIban(outTransactionEntity.getIban());
                    return outTransactionModel;
                })
                .collect(Collectors.toList());
    }


}
