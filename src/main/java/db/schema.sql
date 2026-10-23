DROP TABLE IF EXISTS item_entrega;
DROP TABLE IF EXISTS entrega;
DROP TABLE IF EXISTS endereco;
DROP TABLE IF EXISTS produto;
DROP TABLE IF EXISTS cliente;

CREATE TABLE cliente (
                         id        SERIAL PRIMARY KEY,
                         documento VARCHAR(14) NOT NULL UNIQUE,
                         telefone  CHAR(11)    NOT NULL,
                         nome      VARCHAR(50) NOT NULL
);

CREATE TABLE endereco (
                          id         SERIAL PRIMARY KEY,
                          cliente_id INTEGER     NOT NULL,
                          cep        CHAR(8)     NOT NULL,
                          rua        VARCHAR(50) NOT NULL,
                          numero     VARCHAR(8)  NOT NULL,
                          bairro     VARCHAR(50) NOT NULL,
                          cidade     VARCHAR(50) NOT NULL,
                          estado     CHAR(2)     NOT NULL,

                          FOREIGN KEY (cliente_id) REFERENCES cliente (id)
);

CREATE TABLE produto (
                         id          SERIAL PRIMARY KEY,
                         nome        VARCHAR(100)   NOT NULL,
                         valor       NUMERIC(10, 2) NOT NULL,
                         peso        NUMERIC(10, 3) NOT NULL,
                         comprimento NUMERIC(10, 2) NOT NULL,
                         altura      NUMERIC(10, 2) NOT NULL,
                         largura     NUMERIC(10, 2) NOT NULL
);

CREATE TABLE entrega (
                         id              SERIAL PRIMARY KEY,
                         valor           NUMERIC(10, 2) NOT NULL,
                         status          VARCHAR(50)    NOT NULL,
                         remetente_id    INTEGER        NOT NULL,
                         destinatario_id INTEGER        NOT NULL,
                         origem_id       INTEGER        NOT NULL,
                         destino_id      INTEGER        NOT NULL,

                         FOREIGN KEY (remetente_id) REFERENCES cliente (id),
                         FOREIGN KEY (destinatario_id) REFERENCES cliente (id),
                         FOREIGN KEY (origem_id) REFERENCES endereco (id),
                         FOREIGN KEY (destino_id) REFERENCES endereco (id),

                         CHECK (remetente_id <> destinatario_id)
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
