package com.paymybuddy.app;

import com.paymybuddy.app.dal.entity.*;
import com.paymybuddy.app.dal.repository.InTransactionRepository;
import com.paymybuddy.app.dal.repository.OutTransactionRepository;
import com.paymybuddy.app.dal.repository.TransactionRepository;
import com.paymybuddy.app.dal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private InTransactionRepository inTransactionRepository;
    @Autowired
    private OutTransactionRepository outTransactionRepository;

    public static void main(String[] args) {
        SpringApplication.run(PayMyBuddyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("users");
        for (UserEntity userEntity : userRepository.findAll()) {
            String message = userEntity.getId() + " " + userEntity.getEmail() + " " + userEntity.getPassword();
            for (ConnectionEntity connectionEntity : userEntity.getConnectionsEntity()) {
                message = message + " " + connectionEntity.getConnectedId();
            }
            log.info(message);
        }
        log.info("transactions");
        for (TransactionEntity transactionEntity : transactionRepository.findAll()) {
            String message = transactionEntity.getId() + " "
                    + transactionEntity.getDescription() + " "
                    + transactionEntity.getMonetizedAmount() + " "
                    + transactionEntity.getUserId();
            log.info(message);
        }

        log.info("inTransactions");
        for (InTransactionEntity inTransactionEntity : inTransactionRepository.findAll()) {
            String message = inTransactionEntity.getId() + " "
                    + inTransactionEntity.getGivenAmount() + " "
                    + inTransactionEntity.getTransactionsEntity().getId() + " "
                    + inTransactionEntity.getTransactionsEntity().getDescription() + " "
                    + inTransactionEntity.getTransactionsEntity().getMonetizedAmount() + " "
                    + inTransactionEntity.getTransactionsEntity().getUserId();
            log.info(message);
        }

        log.info("outTransactions");
        for (OutTransactionEntity outTransactionEntity : outTransactionRepository.findAll()) {
            String message = outTransactionEntity.getId() + " "
                    + outTransactionEntity.getTransferredAmount() + " "
                    + outTransactionEntity.getIban() + " "
                    + outTransactionEntity.getTransactionsEntity().getId() + " "
                    + outTransactionEntity.getTransactionsEntity().getDescription() + " "
                    + outTransactionEntity.getTransactionsEntity().getMonetizedAmount() + " "
                    + outTransactionEntity.getTransactionsEntity().getUserId();
            log.info(message);
        }


    }
}
