package com.paymybuddy.app.domain.service;

import com.paymybuddy.app.dal.entity.ConnectionEntity;
import com.paymybuddy.app.dal.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {

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
