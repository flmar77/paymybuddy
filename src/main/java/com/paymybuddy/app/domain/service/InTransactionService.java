package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.InTransactionEntity;
import com.paymybuddy.app.dal.repository.InTransactionRepository;
import com.paymybuddy.app.domain.model.InTransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InTransactionService {

    @Autowired
    private InTransactionRepository inTransactionRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public InTransactionModel createInTransaction(InTransactionModel inTransactionModelToSave) throws UnsupportedOperationException {

        // check balance
        if (userService.getUserBalanceByEmail(inTransactionModelToSave.getConnectorEmail()) < inTransactionModelToSave.getGivenAmount()) {
            throw new UnsupportedOperationException();
        }

        // add InTransaction
        InTransactionEntity inTransactionEntityToSave = new InTransactionEntity();
        inTransactionEntityToSave.setDescription(inTransactionModelToSave.getDescription());
        inTransactionEntityToSave.setGivenAmount(inTransactionModelToSave.getGivenAmount());
        inTransactionEntityToSave.setMonetizedAmount(0);
        inTransactionEntityToSave.setConnectorId(userService.getUserIdByEmail(inTransactionModelToSave.getConnectorEmail()));
        inTransactionEntityToSave.setConnectedId(userService.getUserIdByEmail(inTransactionModelToSave.getConnectedEmail()));

        InTransactionEntity inTransactionEntitySaved = inTransactionRepository.save(inTransactionEntityToSave);

        // update balance
        userService.updateUserBalanceByEmail(inTransactionModelToSave.getConnectorEmail(), -(inTransactionModelToSave.getGivenAmount()));
        userService.updateUserBalanceByEmail(inTransactionModelToSave.getConnectedEmail(), inTransactionModelToSave.getGivenAmount());

        return mapInTransactionEntityToInTransactionModel(inTransactionEntitySaved);

    }

    public List<InTransactionModel> mapInTransactionEntityListToInTransactionModelList(List<InTransactionEntity> inTransactionEntityList) {
        if (inTransactionEntityList == null) {
            return null;
        }
        return inTransactionEntityList.stream()
                .map(this::mapInTransactionEntityToInTransactionModel)
                .collect(Collectors.toList());
    }

    public InTransactionModel mapInTransactionEntityToInTransactionModel(InTransactionEntity inTransactionEntity) {
        InTransactionModel inTransactionModel = new InTransactionModel();
        inTransactionModel.setId(inTransactionEntity.getId());
        inTransactionModel.setDescription(inTransactionEntity.getDescription());
        inTransactionModel.setMonetizedAmount(inTransactionEntity.getMonetizedAmount());
        inTransactionModel.setGivenAmount(inTransactionEntity.getGivenAmount());
        inTransactionModel.setConnectorEmail(userService.getUserEmailById(inTransactionEntity.getConnectorId()));
        inTransactionModel.setConnectedEmail(userService.getUserEmailById(inTransactionEntity.getConnectedId()));
        return inTransactionModel;
    }
}
