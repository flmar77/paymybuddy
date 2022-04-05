package com.paymybuddy.app.dal.repository;

import com.paymybuddy.app.dal.entity.InTransactionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InTransactionRepository extends CrudRepository<InTransactionEntity, Integer> {
}
