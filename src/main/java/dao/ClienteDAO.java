package dao;

import db.ConnectionFactory;
import model.Clientes;
import model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClienteDAO {

    private Connection connection;

    public ClienteDAO (Connection connection ) {
        this.connection = connection;
    }

    public void salvar (Clientes cliente, int enderecoId) {
         String sql = "INSERT INTO cliente (documento, telefone, nome, endereco_id )" +
                 "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, cliente.getDocumento());
            preparedStatement.setString(2, cliente.getTelefone());
            preparedStatement.setString(3, cliente.getNome());
            preparedStatement.setInt(4, enderecoId);

            preparedStatement.execute();

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


}
