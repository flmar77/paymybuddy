package com.paymybuddy.app.dal.repository;

import com.paymybuddy.app.dal.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT email FROM \"user\"", nativeQuery = true)
    List<String> findAllEmails();
}
