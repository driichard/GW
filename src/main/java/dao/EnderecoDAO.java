package dao;

import model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EnderecoDAO {
    private Connection connection;

    public EnderecoDAO(Connection connection) {
        this.connection = connection;
    }

    public int salvar(Endereco endereco, int clienteId) {
        String sql = "INSERT INTO endereco (cliente_id, cep, rua, numero, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, clienteId);
            preparedStatement.setString(2, endereco.getCep());
            preparedStatement.setString(3, endereco.getRua());
            preparedStatement.setString(4, endereco.getNumero());
            preparedStatement.setString(5, endereco.getBairro());
            preparedStatement.setString(6, endereco.getCidade());
            preparedStatement.setString(7, endereco.getEstado());
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

    public void atualizar(Endereco endereco) {
        String sql = "UPDATE endereco SET cep = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, endereco.getCep());
            preparedStatement.setString(2, endereco.getRua());
            preparedStatement.setString(3, endereco.getNumero());
            preparedStatement.setString(4, endereco.getBairro());
            preparedStatement.setString(5, endereco.getCidade());
            preparedStatement.setString(6, endereco.getEstado());
            preparedStatement.setInt(7, endereco.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Endereco buscarPorCliente(int clienteId) {
        String sql = "SELECT id, cep, rua, numero, bairro, cidade, estado FROM endereco WHERE cliente_id = ? ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, clienteId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Endereco montar(ResultSet rs) throws SQLException {
        return new Endereco(
                rs.getInt("id"),
                rs.getString("cep"),
                rs.getString("rua"),
                rs.getString("numero"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("estado")
        );
    }
}
