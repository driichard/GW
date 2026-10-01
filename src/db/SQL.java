package db;

public class SQL {
   /* DROP TABLE cliente;

    CREATE TABLE cliente (
            documento CHAR (11) PRIMARY KEY,
    telefone CHAR (11) NOT NULL,
    nome CHAR (50) NOT NULL,
    endereco_id INTEGER NOT NULL,
    FOREIGN KEY (endereco_id) REFERENCES endereco (id)
            );

    CREATE TABLE endereco (
            id SERIAL PRIMARY KEY,
            cep CHAR (8) NOT NULL,
    rua CHAR (50) NOT NULL,
    numero VARCHAR (8) NOT NULL,
    cidade VARCHAR (50) NOT NULL,
    bairro VARCHAR (50) NOT NULL,
    estado VARCHAR (50) NOT NULL
);

    CREATE TABLE produto (
            id INTEGER PRIMARY KEY,
            nome_do_produto CHAR (100) NOT NULL,
    valor NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL
);

    CREATE TABLE entrega (
            id INTEGER PRIMARY KEY,
            valor_entrega NUMERIC(10,2) NOT NULL,
    status CHAR (50) NOT NULL,
    produto_id INTEGER NOT NULL,
    clientes_documento CHAR (14) NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produto (id),
    FOREIGN KEY (documento) REFERENCES produto (documento)
            ); */
}
