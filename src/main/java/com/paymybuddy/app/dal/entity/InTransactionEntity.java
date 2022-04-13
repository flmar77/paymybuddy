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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    @Column(name = "monetized_amount")
    private float monetizedAmount;

    @Column(name = "given_amount")
    private float givenAmount;

    @Column(name = "connector_id")
    private Integer connectorId;

    @Column(name = "connected_id")
    private Integer connectedId;

}
