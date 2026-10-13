package controller;

import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Produto;
import validation.ValidadorProduto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SistemaProduto {

    public void cadastrarProduto(Scanner input) {
        System.out.println("Nome do produto:");
        String nome = input.nextLine();

        System.out.println("Código do produto:");
        int codigo = input.nextInt();

        System.out.println("Peso (kg):");
        double peso = input.nextDouble();

        System.out.println("Comprimento (cm):");
        double comprimento = input.nextDouble();

        System.out.println("Altura (cm):");
        double altura = input.nextDouble();

        System.out.println("Largura (cm):");
        double largura = input.nextDouble();

        System.out.println("Valor declarado (quanto vale a mercadoria):");
        double valor = input.nextDouble();
        input.nextLine();

        Produto produto = new Produto(codigo, nome, valor, peso, comprimento, altura, largura);

        ValidadorProduto validadorProduto = new ValidadorProduto();
        List<String> errosProduto = validadorProduto.validar(produto);

        if (!errosProduto.isEmpty()) {
            for (String erro : errosProduto) {
                System.out.println(erro);
            }
            System.out.println("===== Cadastre o produto novamente =====");
        } else {
            try (Connection connection = new ConnectionFactory().recuperarConexao()) {
                new ProdutoDAO(connection).salvar(produto);
                System.out.println("Volume: " + produto.getVolume() + " m³");
                System.out.println("===== Produto cadastrado com sucesso!!! =====");
            } catch (SQLException | RuntimeException e) {
                System.out.println("Erro ao gravar produto: " + mensagemBanco(e));
            }
        }
    }

    public List<Produto> getProdutos() {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new ProdutoDAO(connection).listarTodas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Produto buscarPorCodigo(int codigo) {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new ProdutoDAO(connection).buscarPorCodigo(codigo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        if (causa != null && causa.getMessage() != null && causa.getMessage().contains("duplicate")) {
            return "já existe um produto com esse código";
        }
        return e.getMessage();
    }
}
