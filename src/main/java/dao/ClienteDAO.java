package dao;

import model.Cliente;
import model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection connection;

    public ClienteDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (documento, telefone, nome) VALUES (?, ?, ?)";

        try {
            connection.setAutoCommit(false);
            int clienteId;

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, cliente.getDocumento());
                preparedStatement.setString(2, cliente.getTelefone());
                preparedStatement.setString(3, cliente.getNome());
                preparedStatement.execute();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new RuntimeException("Não foi possível gravar o cliente");
                    }
                    clienteId = rs.getInt(1);
                }
            }

            new EnderecoDAO(connection).salvar(cliente.getEndereco(), clienteId);
            connection.commit();
        } catch (SQLException | RuntimeException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackErro) {
                throw new RuntimeException(rollbackErro);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Cliente> listarTodas() {
        String sql = "SELECT c.id, c.documento, c.telefone, c.nome "
                + "FROM cliente c ORDER BY c.nome";
        List<Cliente> clientes = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            EnderecoDAO enderecoDAO = new EnderecoDAO(connection);
            while (rs.next()) {
                clientes.add(montarCliente(rs, enderecoDAO.listarPorCliente(rs.getInt("id"))));
            }
            return clientes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cliente buscarPorDocumento(String documento) {
        String sql = "SELECT id, documento, telefone, nome FROM cliente WHERE documento = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, documento);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    List<Endereco> enderecos = new EnderecoDAO(connection).listarPorCliente(rs.getInt("id"));
                    return montarCliente(rs, enderecos);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(String documentoAtual, Cliente cliente) {
        String sql = "UPDATE cliente SET documento = ?, telefone = ?, nome = ? WHERE documento = ?";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, cliente.getDocumento());
                preparedStatement.setString(2, cliente.getTelefone());
                preparedStatement.setString(3, cliente.getNome());
                preparedStatement.setString(4, documentoAtual);
                preparedStatement.executeUpdate();
            }

            Endereco endereco = cliente.getEndereco();
            if (endereco != null && endereco.getId() > 0) {
                new EnderecoDAO(connection).atualizar(endereco);
            }
            connection.commit();
        } catch (SQLException | RuntimeException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackErro) {
                throw new RuntimeException(rollbackErro);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deletar(String documento) {
        int clienteId = buscarIdPorDocumento(documento);
        String sqlEndereco = "DELETE FROM endereco WHERE cliente_id = ?";
        String sqlCliente = "DELETE FROM cliente WHERE documento = ?";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteEndereco = connection.prepareStatement(sqlEndereco);
                 PreparedStatement deleteCliente = connection.prepareStatement(sqlCliente)) {
                deleteEndereco.setInt(1, clienteId);
                deleteEndereco.executeUpdate();
                deleteCliente.setString(1, documento);
                deleteCliente.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackErro) {
                throw new RuntimeException(rollbackErro);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException erro) {
                throw new RuntimeException(erro);
            }
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

    private Cliente montarCliente(ResultSet rs, List<Endereco> enderecos) throws SQLException {
        return new Cliente(
                rs.getString("telefone"),
                rs.getString("documento"),
                rs.getString("nome"),
                enderecos
        );
    }
}
