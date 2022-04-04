package com.paymybuddy.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "in_transaction", schema = "public")
public class InTransactionEntity {

    @Id
    private Integer id;

    @Column(name = "given_amount")
    private float givenAmount;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private TransactionEntity transactionsEntity;

}
