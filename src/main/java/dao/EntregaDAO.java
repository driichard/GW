package dao;

import model.Cliente;
import model.Endereco;
import model.Entrega;
import model.ItemEntrega;
import model.Produto;
import model.StatusEntrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {
    private Connection connection;

    public EntregaDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Entrega entrega) {
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        int remetenteId = clienteDAO.buscarIdPorDocumento(entrega.getRemetente().getDocumento());
        int destinatarioId = clienteDAO.buscarIdPorDocumento(entrega.getDestinatario().getDocumento());

        String sqlEntrega = "INSERT INTO entrega (status, remetente_id, destinatario_id, origem_id, destino_id) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_entrega (entrega_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";

        try {
            int entregaId;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEntrega, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, entrega.getStatus().name());
                preparedStatement.setInt(2, remetenteId);
                preparedStatement.setInt(3, destinatarioId);
                preparedStatement.setInt(4, entrega.getOrigem().getId());
                preparedStatement.setInt(5, entrega.getDestino().getId());
                preparedStatement.executeUpdate();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new RuntimeException("Não foi possível gravar a entrega");
                    }
                    entregaId = rs.getInt(1);
                    entrega.setId(entregaId);
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlItem)) {
                for (ItemEntrega item : entrega.getItens()) {
                    preparedStatement.setInt(1, entregaId);
                    preparedStatement.setInt(2, item.getProduto().getId());
                    preparedStatement.setInt(3, item.getQuantidade());
                    preparedStatement.setDouble(4, item.getValorUnitario());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizarEntrega(Entrega entrega) {
        ClienteDAO clienteDAO = new ClienteDAO(connection);
        int remetenteId = clienteDAO.buscarIdPorDocumento(entrega.getRemetente().getDocumento());
        int destinatarioId = clienteDAO.buscarIdPorDocumento(entrega.getDestinatario().getDocumento());

        String sqlEntrega = "UPDATE entrega SET remetente_id = ?, destinatario_id = ?, origem_id = ?, destino_id = ? WHERE id = ?";
        String sqlApagaItem = "DELETE FROM item_entrega WHERE entrega_id = ?";
        String sqlItem = "INSERT INTO item_entrega (entrega_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlApagaItem)) {
                preparedStatement.setInt(1, entrega.getId());
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEntrega)) {
                preparedStatement.setInt(1, remetenteId);
                preparedStatement.setInt(2, destinatarioId);
                preparedStatement.setInt(3, entrega.getOrigem().getId());
                preparedStatement.setInt(4, entrega.getDestino().getId());
                preparedStatement.setInt(5, entrega.getId());
                preparedStatement.executeUpdate();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlItem)) {
                for (ItemEntrega item : entrega.getItens()) {
                    preparedStatement.setInt(1, entrega.getId());
                    preparedStatement.setInt(2, item.getProduto().getId());
                    preparedStatement.setInt(3, item.getQuantidade());
                    preparedStatement.setDouble(4, item.getValorUnitario());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Entrega entrega) {
        String sql = "UPDATE entrega SET status = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entrega.getStatus().name());
            preparedStatement.setInt(2, entrega.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(int id) {
        String sqlItem = "DELETE FROM item_entrega WHERE entrega_id = ?";
        String sqlEntrega = "DELETE FROM entrega WHERE id = ?";

        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteItem = connection.prepareStatement(sqlItem);
                 PreparedStatement deleteEntrega = connection.prepareStatement(sqlEntrega)) {
                deleteItem.setInt(1, id);
                deleteItem.executeUpdate();
                deleteEntrega.setInt(1, id);
                deleteEntrega.executeUpdate();
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Entrega buscarPorId(int id) {
        String sql = sqlEntrega() + " WHERE e.id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return montarEntrega(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Entrega> listarTodas() {
        String sql = sqlEntrega() + " ORDER BY e.id";
        List<Entrega> entregas = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                entregas.add(montarEntrega(rs));
            }
            return entregas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String sqlEntrega() {
        return "SELECT e.id, e.status, "
                + "r.documento AS remetente_documento, r.telefone AS remetente_telefone, r.nome AS remetente_nome, "
                + "ro.id AS remetente_endereco_id, ro.cep AS remetente_cep, ro.rua AS remetente_rua, ro.numero AS remetente_numero, "
                + "ro.bairro AS remetente_bairro, ro.cidade AS remetente_cidade, ro.estado AS remetente_estado, "
                + "d.documento AS destinatario_documento, d.telefone AS destinatario_telefone, d.nome AS destinatario_nome, "
                + "de.id AS destinatario_endereco_id, de.cep AS destinatario_cep, de.rua AS destinatario_rua, de.numero AS destinatario_numero, "
                + "de.bairro AS destinatario_bairro, de.cidade AS destinatario_cidade, de.estado AS destinatario_estado "
                + "FROM entrega e "
                + "JOIN cliente r ON r.id = e.remetente_id "
                + "JOIN endereco ro ON ro.id = e.origem_id "
                + "JOIN cliente d ON d.id = e.destinatario_id "
                + "JOIN endereco de ON de.id = e.destino_id";
    }

    private Entrega montarEntrega(ResultSet rs) throws SQLException {
        List<ItemEntrega> itens = listarItens(rs.getInt("id"));
        Endereco origem = montarEndereco(rs, "remetente");
        Endereco destino = montarEndereco(rs, "destinatario");
        return new Entrega(
                rs.getInt("id"),
                montarCliente(rs, "remetente", origem),
                montarCliente(rs, "destinatario", destino),
                origem,
                destino,
                itens,
                statusDe(rs.getString("status"))
        );
    }

    private Cliente montarCliente(ResultSet rs, String prefixo, Endereco endereco) throws SQLException {
        return new Cliente(
                rs.getString(prefixo + "_telefone"),
                rs.getString(prefixo + "_documento"),
                rs.getString(prefixo + "_nome"),
                endereco
        );
    }

    private Endereco montarEndereco(ResultSet rs, String prefixo) throws SQLException {
        return new Endereco(
                rs.getInt(prefixo + "_endereco_id"),
                rs.getString(prefixo + "_cep"),
                rs.getString(prefixo + "_rua"),
                rs.getString(prefixo + "_numero"),
                rs.getString(prefixo + "_bairro"),
                rs.getString(prefixo + "_cidade"),
                rs.getString(prefixo + "_estado")
        );
    }

    private List<ItemEntrega> listarItens(int entregaId) throws SQLException {
        String sql = "SELECT p.id, p.codigo, p.nome, p.valor, p.peso, p.comprimento, p.altura, p.largura, "
                + "i.quantidade, i.valor_unitario "
                + "FROM item_entrega i "
                + "JOIN produto p ON p.id = i.produto_id "
                + "WHERE i.entrega_id = ?";
        List<ItemEntrega> itens = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entregaId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("nome"),
                            rs.getDouble("valor"),
                            rs.getDouble("peso"),
                            rs.getDouble("comprimento"),
                            rs.getDouble("altura"),
                            rs.getDouble("largura")
                    );
                    itens.add(new ItemEntrega(produto, rs.getInt("quantidade"), rs.getDouble("valor_unitario")));
                }
            }
        }
        return itens;
    }

    private StatusEntrega statusDe(String texto) {
        for (StatusEntrega status : StatusEntrega.values()) {
            if (status.toString().equals(texto) || status.name().equals(texto)) {
                return status;
            }
        }
        return null;
    }
}
