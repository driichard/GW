package dao;

import model.Cliente;
import model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Cliente cliente) {
        EnderecoDAO enderecoDAO = new EnderecoDAO(connection);
        int enderecoId = enderecoDAO.salvar(cliente.getEndereco());

        String sql = "INSERT INTO cliente (documento, telefone, nome, endereco_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cliente.getDocumento());
            preparedStatement.setString(2, cliente.getTelefone());
            preparedStatement.setString(3, cliente.getNome());
            preparedStatement.setInt(4, enderecoId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> listarTodas() {
        String sql = "SELECT c.documento, c.telefone, c.nome, e.cep, e.rua, e.numero, e.bairro, e.cidade, e.estado "
                + "FROM cliente c "
                + "JOIN endereco e ON e.id = c.endereco_id";
        List<Cliente> clientes = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                clientes.add(montarCliente(rs));
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cliente buscarPorDocumento(String documento) {
        String sql = "SELECT c.documento, c.telefone, c.nome, e.cep, e.rua, e.numero, e.bairro, e.cidade, e.estado "
                + "FROM cliente c "
                + "JOIN endereco e ON e.id = c.endereco_id "
                + "WHERE c.documento = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, documento);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return montarCliente(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Cliente cliente) {
        String sql = "UPDATE cliente SET telefone = ?, nome = ? WHERE documento = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cliente.getTelefone());
            preparedStatement.setString(2, cliente.getNome());
            preparedStatement.setString(3, cliente.getDocumento());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(String documento) {
        String sql = "DELETE FROM cliente WHERE documento = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, documento);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    int buscarIdPorDocumento(String documento) {
        String sql = "SELECT id FROM cliente WHERE documento = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, documento);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            throw new RuntimeException("Cliente não encontrado");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Cliente montarCliente(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getString("cep"),
                rs.getString("rua"),
                rs.getString("numero"),
                rs.getString("bairro"),
                rs.getString("cidade"),
                rs.getString("estado")
        );

        return new Cliente(
                rs.getString("telefone"),
                rs.getString("documento"),
                rs.getString("nome"),
                endereco
        );
    }
}
