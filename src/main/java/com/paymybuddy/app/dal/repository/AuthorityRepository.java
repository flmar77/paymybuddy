package com.paymybuddy.app.dal.repository;

import com.paymybuddy.app.dal.entity.AuthorityEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<AuthorityEntity, Integer> {
}
