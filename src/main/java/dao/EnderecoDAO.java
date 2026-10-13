package dao;

import model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO {
    private Connection connection;

    public EnderecoDAO(Connection connection) {
        this.connection = connection;
    }

    public int salvar(Endereco endereco) {
        String sql = "INSERT INTO endereco (cep, rua, numero, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, endereco.getCep());
            preparedStatement.setString(2, endereco.getRua());
            preparedStatement.setString(3, endereco.getNumero());
            preparedStatement.setString(4, endereco.getBairro());
            preparedStatement.setString(5, endereco.getCidade());
            preparedStatement.setString(6, endereco.getEstado());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            throw new RuntimeException("Não foi possível gravar o endereço");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Endereco> listarTodas() {
        String sql = "SELECT cep, rua, numero, bairro, cidade, estado FROM endereco";
        List<Endereco> enderecos = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                enderecos.add(new Endereco(
                        rs.getString("cep"),
                        rs.getString("rua"),
                        rs.getString("numero"),
                        rs.getString("bairro"),
                        rs.getString("cidade"),
                        rs.getString("estado")
                ));
            }
            return enderecos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
