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
    private Integer id;

    @Column(name = "transferred_amount")
    private float transferredAmount;

    private String iban;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private TransactionEntity transactionsEntity;

}
