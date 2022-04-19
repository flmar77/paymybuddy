INSERT INTO "user" (email, password, enabled, balance)
VALUES ('user1@mail.com', '$2y$10$KqeVD1LGG5PKjzWL8F8zwuYA7adTmgMuyf6fhtxDn8uRG.nH.mOTW', true, 496.5),
       ('user2@mail.com', '$2y$10$kKNkGiDL65WXFJ0yxjBiS.wJQR.PbE5MLVzWDpsdfhTaVRSbyEgTm', true, 148.25),
       ('user3@mail.com', '$2y$10$ruItfZjGLKHdCeNAOIFo8e5WXEAyNIzdGcdyEnBPfcSkjfH6Y4GfG', true, 0);
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
VALUES ('intrans1', 0.25, 49.75, 1, 6),
       ('intrans2', 0.5, 99.5, 1, 6),
       ('intrans3', 1, 199, 6, 1);
INSERT INTO "out_transaction" (description, monetized_amount, transferred_amount, iban, user_id)
VALUES ('outtrans1', 2.5, 497.5, 'XXX', 1),
       ('outtrans2', 0.25, -49.75, 'XXX', 1),
       ('outtrans3', 1, 199, 'YYY', 6);