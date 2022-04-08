INSERT INTO "user" (id, email, password, enabled, balance)
VALUES (1, 'user1@mail.com', 'user1', true, 500),
       (2, 'user2@mail.com', 'user2', true, 1000);
INSERT INTO authorities (id, user_id, authority)
values (1, 1, 'USER'),
       (2, 2, 'USER');
INSERT INTO "connection" (id, connector_id, connected_id)
VALUES (1, 1, 2),
       (2, 2, 1);
INSERT INTO "in_transaction" (id, description, monetized_amount, given_amount, connector_id, connected_id)
VALUES (1, 'intrans1', 1.5, 50, 1, 2),
       (2, 'intrans2', 2.5, 51, 1, 2),
       (3, 'intrans3', 3.5, 52, 2, 1);
INSERT INTO "out_transaction" (id, description, monetized_amount, transferred_amount, iban, user_id)
VALUES (1, 'outtrans1', 1.1, 100, 'XXX', 1),
       (2, 'outtrans2', 2.1, -50, 'XXX', 1),
       (3, 'outtrans3', 3.1, 200, 'XXX', 2);