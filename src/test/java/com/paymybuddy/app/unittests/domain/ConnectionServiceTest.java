package com.paymybuddy.app.unittests.domain;

import com.paymybuddy.app.dal.entity.ConnectionEntity;
import com.paymybuddy.app.dal.repository.ConnectionRepository;
import com.paymybuddy.app.domain.service.ConnectionService;
import com.paymybuddy.app.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionServiceTest {

    @InjectMocks
    private ConnectionService connectionService;

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UserService userService;

    @Test
    public void should_returnSavedConnection_whenCreateConnectionFromEmails() {
        when(connectionRepository.save(any())).thenReturn(getFakeConnectionEntity());
        when(userService.getUserIdByEmail(anyString())).thenReturn(1);

        ConnectionEntity connectionEntity = connectionService.CreateConnectionFromEmails("xxx", "yyy");

        assertThat(connectionEntity).isNotNull();
        assertThat(connectionEntity.getConnectorId()).isEqualTo(1);
        verify(connectionRepository, times(1)).save(any());
    }

    private ConnectionEntity getFakeConnectionEntity() {
        ConnectionEntity connectionEntity = new ConnectionEntity();
        connectionEntity.setConnectorId(1);
        connectionEntity.setConnectedId(2);
        return connectionEntity;
    }

}
