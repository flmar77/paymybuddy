INSERT INTO "user" (email, password, enabled, balance)
VALUES ('user1@mail.com', '$2y$10$KqeVD1LGG5PKjzWL8F8zwuYA7adTmgMuyf6fhtxDn8uRG.nH.mOTW', true, 500),
       ('user2@mail.com', '$2y$10$kKNkGiDL65WXFJ0yxjBiS.wJQR.PbE5MLVzWDpsdfhTaVRSbyEgTm', true, 1000),
       ('user3@mail.com', '$2y$10$ruItfZjGLKHdCeNAOIFo8e5WXEAyNIzdGcdyEnBPfcSkjfH6Y4GfG', true, 2000);
INSERT INTO authority (user_id, authority)
values (1, 'USER'),
       (6, 'USER'),
       (11, 'USER');
INSERT INTO "connection" (connector_id, connected_id)
VALUES (1, 6),
       (1, 11),
       (6, 1),
       (11, 6);
INSERT INTO "in_transaction" (description, monetized_amount, given_amount, connector_id, connected_id)
VALUES ('intrans1', 1.5, 50, 1, 6),
       ('intrans2', 2.5, 51, 1, 6),
       ('intrans3', 3.5, 52, 6, 1);
INSERT INTO "out_transaction" (description, monetized_amount, transferred_amount, iban, user_id)
VALUES ('outtrans1', 1.1, 100, 'XXX', 1),
       ('outtrans2', 2.1, -50, 'XXX', 1),
       ('outtrans3', 3.1, 200, 'XXX', 6);