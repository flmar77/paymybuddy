package com.paymybuddy.app.domain.helper;

import com.paymybuddy.app.dal.entity.ConnectionEntity;
import com.paymybuddy.app.dal.repository.ConnectionRepository;
import com.paymybuddy.app.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionHelper {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserService userService;

    public ConnectionEntity CreateConnectionFromEmails(String connectorEmail, String connectedEmail) {
        ConnectionEntity connectionEntityToSave = new ConnectionEntity();
        connectionEntityToSave.setConnectorId(userService.getUserIdByEmail(connectorEmail));
        connectionEntityToSave.setConnectedId(userService.getUserIdByEmail(connectedEmail));
        return connectionRepository.save(connectionEntityToSave);
    }

}
