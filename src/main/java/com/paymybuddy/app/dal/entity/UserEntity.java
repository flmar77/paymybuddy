package com.paymybuddy.app.dal.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user", schema = "public")
public class UserEntity {

    // TODO : generatedValue strat sequence
    @Id
    @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    // TODO : list<connectedId>
    // ManyToMany + JoinTable
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "connector_id")
    private List<ConnectionEntity> connectionsEntity = new ArrayList<>();
}
