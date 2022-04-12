package com.paymybuddy.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "connection", schema = "public")
public class ConnectionEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "connector_id")
    private Integer connectorId;

    @Column(name = "connected_id")
    private Integer connectedId;
}
