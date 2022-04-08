DROP TABLE IF EXISTS "user" CASCADE;
-- TODO : "random" sequence managed by JPA
CREATE TABLE "user"
(
    id       serial PRIMARY KEY,
    email    text UNIQUE    NOT NULL,
    password text           NOT NULL,
    enabled  bool           NOT NULL DEFAULT true,
    balance  numeric(22, 2) NOT NULL CHECK ( balance > 0 )
);

DROP TABLE IF EXISTS authority CASCADE;
CREATE TABLE authority
(
    id        serial PRIMARY KEY,
    user_id   int  NOT NULL,
    authority text NOT NULL DEFAULT 'USER',
    FOREIGN KEY (user_id) REFERENCES "user" (id)
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

DROP TABLE IF EXISTS in_transaction CASCADE;
CREATE TABLE in_transaction
(
    id               serial PRIMARY KEY,
    description      text,
    monetized_amount numeric(22, 2) NOT NULL CHECK ( monetized_amount > 0 ),
    given_amount     numeric(22, 2) NOT NULL CHECK ( given_amount > 0 ),
    connector_id     int            NOT NULL,
    connected_id     int            NOT NULL,
    FOREIGN KEY (connector_id) REFERENCES "user" (id),
    FOREIGN KEY (connected_id) REFERENCES "user" (id)
);

DROP TABLE IF EXISTS out_transaction CASCADE;
CREATE TABLE out_transaction
(
    id                 serial PRIMARY KEY,
    description        text,
    monetized_amount   numeric(22, 2) NOT NULL CHECK ( monetized_amount > 0 ),
    transferred_amount numeric(22, 2) NOT NULL,
    iban               text,
    user_id            int            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);