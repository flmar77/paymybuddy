package com.paymybuddy.app.unittests.domain;

import com.paymybuddy.app.dal.entity.InTransactionEntity;
import com.paymybuddy.app.dal.repository.InTransactionRepository;
import com.paymybuddy.app.domain.helper.MonetizeHelper;
import com.paymybuddy.app.domain.model.InTransactionModel;
import com.paymybuddy.app.domain.service.InTransactionService;
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
public class InTransactionServiceTest {

    @InjectMocks
    private InTransactionService inTransactionService;

    @Mock
    private InTransactionRepository inTransactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private MonetizeHelper monetizeHelper;

    @Test
    public void should_throwUnsupportedOperationException_whenCreateUnsupportedInTransaction() {
        when(userService.getUserBalanceByEmail(anyString())).thenReturn(10f);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> inTransactionService.createInTransaction(getFakeInTransactionModel()));
    }

    @Test
    public void should_createInTransaction_whenCreateValidInTransaction() {
        when(userService.getUserBalanceByEmail(anyString())).thenReturn(100f);
        when(monetizeHelper.getMonetizedAmount(anyFloat())).thenReturn(0.1f);
        when(userService.getUserIdByEmail(anyString())).thenReturn(1);
        when(inTransactionRepository.save(any())).thenReturn(getFakeInTransactionEntity());

        InTransactionModel inTransactionModel = inTransactionService.createInTransaction(getFakeInTransactionModel());

        assertThat(inTransactionModel).isNotNull();
        verify(inTransactionRepository, times(1)).save(any(InTransactionEntity.class));
        verify(userService, times(2)).updateUserBalanceByEmail(anyString(), anyFloat());
    }

    @Test
    public void should_returnNull_whenMapNullInTransactionEntityListToInTransactionModelList() {
        assertThat(inTransactionService.mapInTransactionEntityListToInTransactionModelList(null)).isNull();
    }

    @Test
    public void should_returnSomething_whenMapValidInTransactionEntityListToInTransactionModelList() {
        assertThat(inTransactionService.mapInTransactionEntityListToInTransactionModelList(Collections.singletonList(getFakeInTransactionEntity()))).isNotNull();
    }

    private InTransactionEntity getFakeInTransactionEntity() {
        InTransactionEntity inTransactionEntity = new InTransactionEntity();
        inTransactionEntity.setDescription("desc");
        inTransactionEntity.setMonetizedAmount(0.1f);
        inTransactionEntity.setGivenAmount(19.9f);
        inTransactionEntity.setConnectorId(1);
        inTransactionEntity.setConnectedId(2);
        return inTransactionEntity;
    }

    private InTransactionModel getFakeInTransactionModel() {
        InTransactionModel inTransactionModel = new InTransactionModel();
        inTransactionModel.setId(1);
        inTransactionModel.setDescription("desc");
        inTransactionModel.setMonetizedAmount(0.1f);
        inTransactionModel.setGivenAmount(20f);
        inTransactionModel.setConnectorEmail("xxx@mail.com");
        inTransactionModel.setConnectedEmail("yyy@mail.com");
        return inTransactionModel;
    }

}
