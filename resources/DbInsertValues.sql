INSERT INTO "user" (email, password)
VALUES ('user1@mail.com', 'user1'),
       ('user2@mail.com', 'user2');
INSERT INTO "connection" (connector_id, connected_id)
VALUES (1, 2),
       (2, 1);
INSERT INTO "transaction" (description, monetized_amount, user_id)
VALUES ('trans1', 1.3, 1);
INSERT INTO "transaction" (description, monetized_amount, user_id)
VALUES ('trans2', 3.8, 1);
INSERT INTO "in_transaction"
VALUES (1, 50);
INSERT INTO "out_transaction"
VALUES (2, 200, 'iban1');