package com.paymybuddy.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "out_transaction", schema = "public")
public class OutTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Column(name = "monetized_amount")
    private float monetizedAmount;

    @Column(name = "transferred_amount")
    private float transferredAmount;

    private String iban;

    @Column(name = "user_id")
    private Integer userId;
}
