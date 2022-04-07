package com.paymybuddy.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

// TODO : delete

@Getter
@Setter
@Entity
@Table(name = "connection", schema = "public")
public class ConnectionEntity {

    @Id
    @GeneratedValue
    private int id;

    @Column(name = "connector_id")
    private int connectorId;

    @Column(name = "connected_id")
    private int connectedId;
}
