package com.paymybuddy.app.dal.repository;

import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutTransactionRepository extends CrudRepository<OutTransactionEntity, Integer> {
    List<OutTransactionEntity> findByUserId(Integer id);
}
