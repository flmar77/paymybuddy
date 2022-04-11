package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import com.paymybuddy.app.dal.entity.InTransactionEntity;
import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import com.paymybuddy.app.dal.entity.UserEntity;
import com.paymybuddy.app.dal.repository.UserRepository;
import com.paymybuddy.app.domain.model.InTransactionModel;
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
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    private UserModel mapUserEntityToUserModel(UserEntity userEntity) {
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId());
        userModel.setEmail(userEntity.getEmail());
        userModel.setPassword(userEntity.getPassword());
        userModel.setConnectedEmails(userEntity.getConnectedUsers().stream()
                .map(UserEntity::getEmail)
                .collect(Collectors.toList()));
        userModel.setInTransactionModelList(mapInTransactionEntityToInTransactionModel(userEntity.getInTransactionEntityList()));
        userModel.setOutTransactionModelList(mapOutTransactionEntityToOuTransactionModel(userEntity.getOutTransactionEntityList()));
        userModel.setRoles(userEntity.getAuthorityEntityList().stream()
                .map(AuthorityEntity::getAuthority)
                .collect(Collectors.toList()));
        return userModel;
    }

    private List<InTransactionModel> mapInTransactionEntityToInTransactionModel(List<InTransactionEntity> inTransactionEntityList) {
        if (inTransactionEntityList == null) {
            return null;
        }
        return inTransactionEntityList.stream()
                .map(inTransactionEntity -> {
                    InTransactionModel inTransactionModel = new InTransactionModel();
                    inTransactionModel.setId(inTransactionEntity.getId());
                    inTransactionModel.setDescription(inTransactionEntity.getDescription());
                    inTransactionModel.setMonetizedAmount(inTransactionEntity.getMonetizedAmount());
                    inTransactionModel.setGivenAmount(inTransactionEntity.getGivenAmount());
                    inTransactionModel.setConnectedId(inTransactionEntity.getConnectedId());
                    return inTransactionModel;
                })
                .collect(Collectors.toList());
    }

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

    /*
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

 */

}
