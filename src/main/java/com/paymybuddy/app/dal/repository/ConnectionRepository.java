package com.paymybuddy.app.dal.repository;

import com.paymybuddy.app.dal.entity.ConnectionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends CrudRepository<ConnectionEntity, Integer> {
}
