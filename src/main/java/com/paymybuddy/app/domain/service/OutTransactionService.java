package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import com.paymybuddy.app.dal.repository.OutTransactionRepository;
import com.paymybuddy.app.domain.helper.MonetizeHelper;
import com.paymybuddy.app.domain.model.OutTransactionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OutTransactionService {

    @Autowired
    private OutTransactionRepository outTransactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MonetizeHelper monetizeHelper;

    @Transactional
    public OutTransactionModel createOutTransaction(OutTransactionModel outTransactionModelToSave) {

        // check balance
        if ((userService.getUserBalanceByEmail(outTransactionModelToSave.getUserEmail()) + outTransactionModelToSave.getTransferredAmount()) < 0) {
            throw new UnsupportedOperationException();
        }

        // add OutTransaction
        OutTransactionEntity outTransactionEntityToSave = new OutTransactionEntity();
        outTransactionEntityToSave.setDescription(outTransactionModelToSave.getDescription());
        outTransactionEntityToSave.setIban(outTransactionModelToSave.getIban());
        outTransactionEntityToSave.setMonetizedAmount(monetizeHelper.getMonetizedAmount(outTransactionModelToSave.getTransferredAmount()));
        if (outTransactionModelToSave.getTransferredAmount() < 0) {
            outTransactionEntityToSave.setTransferredAmount(outTransactionModelToSave.getTransferredAmount() + outTransactionEntityToSave.getMonetizedAmount());
        } else {
            outTransactionEntityToSave.setTransferredAmount(outTransactionModelToSave.getTransferredAmount() - outTransactionEntityToSave.getMonetizedAmount());
        }
        outTransactionEntityToSave.setUserId(userService.getUserIdByEmail(outTransactionModelToSave.getUserEmail()));

        OutTransactionEntity outTransactionEntitySaved = outTransactionRepository.save(outTransactionEntityToSave);

        // update balance
        if (outTransactionModelToSave.getTransferredAmount() < 0) {
            userService.updateUserBalanceByEmail(outTransactionModelToSave.getUserEmail(), outTransactionModelToSave.getTransferredAmount());
        } else {
            userService.updateUserBalanceByEmail(outTransactionModelToSave.getUserEmail(), outTransactionEntityToSave.getTransferredAmount());
        }


        return mapOutTransactionEntityToOutTransactionModel(outTransactionEntitySaved);
    }

    public List<OutTransactionModel> mapOutTransactionEntityListToOutTransactionModelList(List<OutTransactionEntity> outTransactionEntityList) {
        if (outTransactionEntityList == null) {
            return null;
        }
        return outTransactionEntityList.stream()
                .map(this::mapOutTransactionEntityToOutTransactionModel)
                .collect(Collectors.toList());
    }

    private OutTransactionModel mapOutTransactionEntityToOutTransactionModel(OutTransactionEntity outTransactionEntity) {
        OutTransactionModel outTransactionModel = new OutTransactionModel();
        outTransactionModel.setId(outTransactionEntity.getId());
        outTransactionModel.setDescription(outTransactionEntity.getDescription());
        outTransactionModel.setMonetizedAmount(outTransactionEntity.getMonetizedAmount());
        outTransactionModel.setTransferredAmount(outTransactionEntity.getTransferredAmount());
        outTransactionModel.setIban(outTransactionEntity.getIban());
        outTransactionModel.setUserEmail(userService.getUserEmailById(outTransactionEntity.getUserId()));
        return outTransactionModel;
    }

}
