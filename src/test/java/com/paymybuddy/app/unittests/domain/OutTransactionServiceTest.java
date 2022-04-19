package com.paymybuddy.app.unittests.domain;

import com.paymybuddy.app.dal.entity.OutTransactionEntity;
import com.paymybuddy.app.dal.repository.OutTransactionRepository;
import com.paymybuddy.app.domain.helper.MonetizeHelper;
import com.paymybuddy.app.domain.model.OutTransactionModel;
import com.paymybuddy.app.domain.service.OutTransactionService;
import com.paymybuddy.app.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OutTransactionServiceTest {

    @InjectMocks
    private OutTransactionService outTransactionService;

    @Mock
    private OutTransactionRepository outTransactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private MonetizeHelper monetizeHelper;

    @Test
    public void should_throwUnsupportedOperationException_whenCreateUnsupportedOutTransaction() {
        when(userService.getUserBalanceByEmail(anyString())).thenReturn(10f);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> outTransactionService.createOutTransaction(getFakeOutTransactionModel(true)));
    }

    @Test
    public void should_createOutTransaction_whenCreateValidNegativeOutTransaction() {
        when(userService.getUserBalanceByEmail(anyString())).thenReturn(100f);
        when(monetizeHelper.getMonetizedAmount(anyFloat())).thenReturn(0.1f);
        when(userService.getUserIdByEmail(anyString())).thenReturn(1);
        when(outTransactionRepository.save(any())).thenReturn(getFakeOutTransactionEntity(true));

        OutTransactionModel outTransactionModel = outTransactionService.createOutTransaction(getFakeOutTransactionModel(true));

        assertThat(outTransactionModel).isNotNull();
        verify(outTransactionRepository, times(1)).save(any(OutTransactionEntity.class));
        verify(userService, times(1)).updateUserBalanceByEmail(anyString(), anyFloat());
    }

    @Test
    public void should_createOutTransaction_whenCreateValidPositiveOutTransaction() {
        when(userService.getUserBalanceByEmail(anyString())).thenReturn(100f);
        when(monetizeHelper.getMonetizedAmount(anyFloat())).thenReturn(0.1f);
        when(userService.getUserIdByEmail(anyString())).thenReturn(1);
        when(outTransactionRepository.save(any())).thenReturn(getFakeOutTransactionEntity(false));

        OutTransactionModel outTransactionModel = outTransactionService.createOutTransaction(getFakeOutTransactionModel(false));

        assertThat(outTransactionModel).isNotNull();
        verify(outTransactionRepository, times(1)).save(any(OutTransactionEntity.class));
        verify(userService, times(1)).updateUserBalanceByEmail(anyString(), anyFloat());
    }

    @Test
    public void should_returnNull_whenMapNullOutTransactionEntityListToOutTransactionModelList() {
        assertThat(outTransactionService.mapOutTransactionEntityListToOutTransactionModelList(null)).isNull();
    }

    @Test
    public void should_returnSomething_whenMapValidOutTransactionEntityListToOutTransactionModelList() {
        assertThat(outTransactionService.mapOutTransactionEntityListToOutTransactionModelList(Collections.singletonList(getFakeOutTransactionEntity(true)))).isNotNull();
    }

    private OutTransactionModel getFakeOutTransactionModel(boolean isNegative) {
        OutTransactionModel outTransactionModel = new OutTransactionModel();
        outTransactionModel.setId(1);
        outTransactionModel.setDescription("desc");
        outTransactionModel.setMonetizedAmount(0.1f);
        if (isNegative) {
            outTransactionModel.setTransferredAmount(-20f);
        } else {
            outTransactionModel.setTransferredAmount(20f);
        }
        outTransactionModel.setIban("iban");
        outTransactionModel.setUserEmail("xxx@mail.com");
        return outTransactionModel;
    }

    private OutTransactionEntity getFakeOutTransactionEntity(boolean isNegative) {
        OutTransactionEntity outTransactionEntity = new OutTransactionEntity();
        outTransactionEntity.setDescription("desc");
        outTransactionEntity.setIban("iban");
        outTransactionEntity.setMonetizedAmount(0.1f);
        if (isNegative) {
            outTransactionEntity.setTransferredAmount(-19.9f);
        } else {
            outTransactionEntity.setTransferredAmount(19.9f);
        }
        outTransactionEntity.setUserId(1);
        return outTransactionEntity;
    }
}
