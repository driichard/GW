package dao;

import model.Produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection connection;

    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Produto produto) {
        String sql = "INSERT INTO produto (codigo, nome, valor, peso, comprimento, altura, largura) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, produto.getCodigo());
            preparedStatement.setString(2, produto.getNomeDoProduto());
            preparedStatement.setDouble(3, produto.getValor());
            preparedStatement.setDouble(4, produto.getPeso());
            preparedStatement.setDouble(5, produto.getComprimento());
            preparedStatement.setDouble(6, produto.getAltura());
            preparedStatement.setDouble(7, produto.getLargura());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produto> listarTodas() {
        String sql = "SELECT codigo, nome, valor, peso, comprimento, altura, largura FROM produto";
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

    public Produto buscarPorCodigo(int codigo) {
        String sql = "SELECT codigo, nome, valor, peso, comprimento, altura, largura FROM produto WHERE codigo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, codigo);

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
        String sql = "UPDATE produto SET nome = ?, valor = ?, peso = ?, comprimento = ?, altura = ?, largura = ? WHERE codigo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, produto.getNomeDoProduto());
            preparedStatement.setDouble(2, produto.getValor());
            preparedStatement.setDouble(3, produto.getPeso());
            preparedStatement.setDouble(4, produto.getComprimento());
            preparedStatement.setDouble(5, produto.getAltura());
            preparedStatement.setDouble(6, produto.getLargura());
            preparedStatement.setInt(7, produto.getCodigo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deletar(int codigo) {
        String sql = "DELETE FROM produto WHERE codigo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, codigo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    int buscarIdPorCodigo(int codigo) {
        String sql = "SELECT id FROM produto WHERE codigo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, codigo);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            throw new RuntimeException("Produto não encontrado");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Produto montarProduto(ResultSet rs) throws SQLException {
        return new Produto(
                rs.getInt("codigo"),
                rs.getString("nome"),
                rs.getDouble("valor"),
                rs.getDouble("peso"),
                rs.getDouble("comprimento"),
                rs.getDouble("altura"),
                rs.getDouble("largura")
        );
    }
}
