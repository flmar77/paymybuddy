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

    private boolean enabled;

    private float balance;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinTable(name = "connection",
            joinColumns = @JoinColumn(name = "connector_id"),
            inverseJoinColumns = @JoinColumn(name = "connected_id")
    )
    private List<UserEntity> connectedUsers = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "connected_id")
    private List<InTransactionEntity> inTransactionEntityList;

    @OneToMany(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id")
    private List<OutTransactionEntity> outTransactionEntityList;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "user_id")
    private List<AuthorityEntity> authorityEntityList;

}
