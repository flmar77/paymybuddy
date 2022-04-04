INSERT INTO "User" (email, password)
VALUES ('user1@mail.com', 'user1'),
       ('user2@mail.com', 'user2');
INSERT INTO "Connection" (connectorId, connectedId)
VALUES (1, 2);
INSERT INTO "Transaction" (description, monetizedAmount, user_id)
VALUES ('trans1', 1.3, 1);
INSERT INTO "Transaction" (description, monetizedAmount, user_id)
VALUES ('trans2', 3.8, 1);
INSERT INTO "InTransaction"
VALUES (1, 50);
INSERT INTO "OutTransaction"
VALUES (2, 200, 'iban1');