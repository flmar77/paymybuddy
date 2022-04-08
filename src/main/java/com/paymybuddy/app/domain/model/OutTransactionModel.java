package com.paymybuddy.app.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OutTransactionModel {

    private Integer id;

    private String description;

    private float monetizedAmount;

    private float transferredAmount;

    private String iban;

}
