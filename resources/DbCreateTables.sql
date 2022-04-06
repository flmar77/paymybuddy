DROP TABLE IF EXISTS "user" CASCADE;
CREATE TABLE "user"
(
    id       serial PRIMARY KEY,
    email    text UNIQUE NOT NULL,
    password text        NOT NULL
);

DROP TABLE IF EXISTS connection CASCADE;
CREATE TABLE connection
(
    id           serial PRIMARY KEY,
    connector_id int NOT NULL,
    connected_id int NOT NULL,
    FOREIGN KEY (connector_id) REFERENCES "user" (id),
    FOREIGN KEY (connected_id) REFERENCES "user" (id),
    UNIQUE (connector_id, connected_id)
);

DROP TABLE IF EXISTS transaction CASCADE;
CREATE TABLE transaction
(
    id               serial PRIMARY KEY,
    description      text,
    monetized_amount numeric(22, 2) NOT NULL,
    user_id          int            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);

DROP TABLE IF EXISTS in_transaction CASCADE;
-- TODO : given_amount could be + or -
CREATE TABLE in_transaction
(
    id           int UNIQUE     NOT NULL PRIMARY KEY,
    given_amount numeric(22, 2) NOT NULL CHECK ( given_amount > 0 ),
    FOREIGN KEY (id) REFERENCES "transaction" (id)
);

DROP TABLE IF EXISTS out_transaction CASCADE;
CREATE TABLE out_transaction
(
    id                 int UNIQUE     NOT NULL PRIMARY KEY,
    transferred_amount numeric(22, 2) NOT NULL,
    iban               text,
    FOREIGN KEY (id) REFERENCES "transaction" (id)
);