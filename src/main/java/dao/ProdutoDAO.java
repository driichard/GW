package dao;

import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Produto produto) {
        String sql = "INSERT INTO produto (nome, valor, peso, comprimento, altura, largura) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, produto.getNomeDoProduto());
            preparedStatement.setDouble(2, produto.getValor());
            preparedStatement.setDouble(3, produto.getPeso());
            preparedStatement.setDouble(4, produto.getComprimento());
            preparedStatement.setDouble(5, produto.getAltura());
            preparedStatement.setDouble(6, produto.getLargura());
            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new RuntimeException("Não foi possível gravar o produto");
                }
                produto.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produto> listarTodas() {
        String sql = "SELECT id, nome, valor, peso, comprimento, altura, largura FROM produto ORDER BY id";
        List<Produto> produtos = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                produtos.add(montarProduto(rs));
            }
            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Produto buscarPorId(int id) {
        String sql = "SELECT id, nome, valor, peso, comprimento, altura, largura FROM produto WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return montarProduto(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizar(Produto produto) {
        String sql = "UPDATE produto SET nome = ?, valor = ?, peso = ?, comprimento = ?, altura = ?, largura = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, produto.getNomeDoProduto());
            preparedStatement.setDouble(2, produto.getValor());
            preparedStatement.setDouble(3, produto.getPeso());
            preparedStatement.setDouble(4, produto.getComprimento());
            preparedStatement.setDouble(5, produto.getAltura());
            preparedStatement.setDouble(6, produto.getLargura());
            preparedStatement.setInt(7, produto.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Produto montarProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getDouble("valor"),
                rs.getDouble("peso"),
                rs.getDouble("comprimento"),
                rs.getDouble("altura"),
                rs.getDouble("largura")
        );
    }
}
