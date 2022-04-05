package com.paymybuddy.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "transaction", schema = "public")
public class TransactionEntity {

    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    @Column(name = "monetized_amount")
    private float monetizedAmount;

    @Column(name = "user_id")
    private int userId;
}
