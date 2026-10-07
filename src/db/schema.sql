CREATE TABLE endereco (
                          id     SERIAL PRIMARY KEY,
                          cep    CHAR(8)     NOT NULL,
                          rua    VARCHAR(50) NOT NULL,
                          numero VARCHAR(8)  NOT NULL,
                          bairro VARCHAR(50) NOT NULL,
                          cidade VARCHAR(50) NOT NULL,
                          estado CHAR(2)     NOT NULL
);
CREATE TABLE cliente (
                         id          SERIAL PRIMARY KEY,
                         documento   VARCHAR(14) NOT NULL UNIQUE,
                         telefone    CHAR(11)    NOT NULL,
                         nome        VARCHAR(50) NOT NULL,
                         endereco_id INTEGER     NOT NULL,

                         FOREIGN KEY (endereco_id) REFERENCES endereco (id)
);
CREATE TABLE produto (
                         id      SERIAL PRIMARY KEY,
                         codigo INTEGER  NOT NULL UNIQUE,
                         nome   VARCHAR(100)   NOT NULL,
                         valor  NUMERIC(10, 2) NOT NULL
);
CREATE TABLE entrega (
                         id            SERIAL PRIMARY KEY,
                         codigo        INTEGER        NOT NULL UNIQUE,
                         valor         NUMERIC(10, 2) NOT NULL,
                         status        VARCHAR(50)    NOT NULL,
                         cliente_id    INTEGER        NOT NULL,

                         FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);
CREATE TABLE item_entrega (
                              id             SERIAL PRIMARY KEY,
                              entrega_id     INTEGER        NOT NULL,
                              produto_id     INTEGER        NOT NULL,
                              quantidade     INTEGER        NOT NULL,
                              valor_unitario NUMERIC(10, 2) NOT NULL,

                              FOREIGN KEY (entrega_id) REFERENCES entrega (id),
                              FOREIGN KEY (produto_id) REFERENCES produto (id),

                              UNIQUE (entrega_id, produto_id)
);