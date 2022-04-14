package com.paymybuddy.app.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserModel {

    private Integer id;

    private String email;

    private String password;

    private float balance;

    private List<String> connectedEmails;

    private List<InTransactionModel> inTransactionModelList;

    private List<OutTransactionModel> outTransactionModelList;

    private List<String> roles;

}
