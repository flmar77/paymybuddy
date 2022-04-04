DROP TABLE IF EXISTS "User" CASCADE;
CREATE TABLE "User"
(
    id       serial PRIMARY KEY,
    email    text UNIQUE NOT NULL,
    password text        NOT NULL
);

DROP TABLE IF EXISTS "Connection" CASCADE;
CREATE TABLE "Connection"
(
    id          serial PRIMARY KEY,
    connectorId int NOT NULL,
    connectedId int not null,
    FOREIGN KEY (connectorId) REFERENCES "User" (id),
    FOREIGN KEY (connectedId) REFERENCES "User" (id),
    UNIQUE (connectorId, connectedId)
);

DROP TABLE IF EXISTS "Transaction" CASCADE;
CREATE TABLE "Transaction"
(
    id              serial PRIMARY KEY,
    description     text,
    monetizedAmount numeric(22, 2) NOT NULL,
    user_id         int            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES "User" (id)
);

DROP TABLE IF EXISTS "InTransaction" CASCADE;
CREATE TABLE "InTransaction"
(
    id          int UNIQUE     NOT NULL PRIMARY KEY,
    givenAmount numeric(22, 2) NOT NULL CHECK ( givenAmount > 0 ),
    FOREIGN KEY (id) REFERENCES "Transaction" (id)
);

DROP TABLE IF EXISTS "OutTransaction" CASCADE;
CREATE TABLE "OutTransaction"
(
    id                int UNIQUE     NOT NULL PRIMARY KEY,
    transferredAmount numeric(22, 2) NOT NULL,
    iban              text,
    FOREIGN KEY (id) REFERENCES "Transaction" (id)
);