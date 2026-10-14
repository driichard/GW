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
        ProdutoDAO produtoDAO = new ProdutoDAO(connection);
        int remetenteId = clienteDAO.buscarIdPorDocumento(entrega.getRemetente().getDocumento());
        int destinatarioId = clienteDAO.buscarIdPorDocumento(entrega.getDestinatario().getDocumento());

        String sqlEntrega = "INSERT INTO entrega (codigo, valor, status, remetente_id, destinatario_id) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_entrega (entrega_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            int entregaId;
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlEntrega, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, entrega.getCodigo());
                preparedStatement.setDouble(2, entrega.getValorEntrega());
                preparedStatement.setString(3, entrega.getStatus().toString());
                preparedStatement.setInt(4, remetenteId);
                preparedStatement.setInt(5, destinatarioId);
                preparedStatement.executeUpdate();

                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new RuntimeException("Não foi possível gravar a entrega");
                    }
                    entregaId = rs.getInt(1);
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlItem)) {
                for (ItemEntrega item : entrega.getItens()) {
                    int produtoId = produtoDAO.buscarIdPorCodigo(item.getProduto().getCodigo());
                    preparedStatement.setInt(1, entregaId);
                    preparedStatement.setInt(2, produtoId);
                    preparedStatement.setInt(3, item.getQuantidade());
                    preparedStatement.setDouble(4, item.getValorUnitario());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
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

    public void atualizar(Entrega entrega) {
        String sql = "UPDATE entrega SET status = ? WHERE codigo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entrega.getStatus().toString());
            preparedStatement.setInt(2, entrega.getCodigo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(int codigo) {
        String sqlItem = "DELETE FROM item_entrega WHERE entrega_id = (SELECT id FROM entrega WHERE codigo = ?)";
        String sqlEntrega = "DELETE FROM entrega WHERE codigo = ?";

        try (PreparedStatement deleteItem = connection.prepareStatement(sqlItem);
             PreparedStatement deleteEntrega = connection.prepareStatement(sqlEntrega)) {
            deleteItem.setInt(1, codigo);
            deleteItem.executeUpdate();
            deleteEntrega.setInt(1, codigo);
            deleteEntrega.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Entrega buscarPorCodigo(int codigo) {
        String sql = sqlEntrega() + " WHERE e.codigo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, codigo);

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
        String sql = sqlEntrega() + " ORDER BY e.codigo";
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
        return "SELECT e.codigo, e.valor, e.status, "
                + "r.documento AS remetente_documento, r.telefone AS remetente_telefone, r.nome AS remetente_nome, "
                + "ro.cep AS remetente_cep, ro.rua AS remetente_rua, ro.numero AS remetente_numero, "
                + "ro.bairro AS remetente_bairro, ro.cidade AS remetente_cidade, ro.estado AS remetente_estado, "
                + "d.documento AS destinatario_documento, d.telefone AS destinatario_telefone, d.nome AS destinatario_nome, "
                + "de.cep AS destinatario_cep, de.rua AS destinatario_rua, de.numero AS destinatario_numero, "
                + "de.bairro AS destinatario_bairro, de.cidade AS destinatario_cidade, de.estado AS destinatario_estado "
                + "FROM entrega e "
                + "JOIN cliente r ON r.id = e.remetente_id "
                + "JOIN endereco ro ON ro.id = r.endereco_id "
                + "JOIN cliente d ON d.id = e.destinatario_id "
                + "JOIN endereco de ON de.id = d.endereco_id";
    }

    private Entrega montarEntrega(ResultSet rs) throws SQLException {
        List<ItemEntrega> itens = listarItens(rs.getInt("codigo"));
        return new Entrega(
                rs.getInt("codigo"),
                montarCliente(rs, "remetente"),
                montarCliente(rs, "destinatario"),
                itens,
                rs.getDouble("valor"),
                statusDe(rs.getString("status"))
        );
    }

    private Cliente montarCliente(ResultSet rs, String prefixo) throws SQLException {
        return new Cliente(
                rs.getString(prefixo + "_telefone"),
                rs.getString(prefixo + "_documento"),
                rs.getString(prefixo + "_nome"),
                new Endereco(
                        rs.getString(prefixo + "_cep"),
                        rs.getString(prefixo + "_rua"),
                        rs.getString(prefixo + "_numero"),
                        rs.getString(prefixo + "_bairro"),
                        rs.getString(prefixo + "_cidade"),
                        rs.getString(prefixo + "_estado")
                )
        );
    }

    private List<ItemEntrega> listarItens(int codigoEntrega) throws SQLException {
        String sql = "SELECT p.codigo, p.nome, p.valor, p.peso, p.comprimento, p.altura, p.largura, "
                + "i.quantidade, i.valor_unitario "
                + "FROM item_entrega i "
                + "JOIN produto p ON p.id = i.produto_id "
                + "JOIN entrega e ON e.id = i.entrega_id "
                + "WHERE e.codigo = ?";
        List<ItemEntrega> itens = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, codigoEntrega);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getInt("codigo"),
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
