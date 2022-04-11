package com.paymybuddy.app.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InTransactionModel {

    private Integer id;

    private String description;

    private float monetizedAmount;

    private float givenAmount;

    private String connectedEmail;

}
