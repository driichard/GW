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
        System.out.println("Nome da caixa:");
        String nome = input.nextLine();

        System.out.println("Código (você escolhe o número):");
        int codigo = input.nextInt();

        System.out.println("Peso (kg):");
        double peso = input.nextDouble();

        System.out.println("Comprimento (cm):");
        double comprimento = input.nextDouble();

        System.out.println("Altura (cm):");
        double altura = input.nextDouble();

        System.out.println("Largura (cm):");
        double largura = input.nextDouble();

        System.out.println("Valor declarado (não entra no frete):");
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
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new ProdutoDAO(connection).salvar(produto);
            System.out.println("Volume (C × A × L): " + produto.getVolume() + " m³");
            System.out.println("===== Produto cadastrado com sucesso!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao gravar produto: " + mensagemBanco(e));
        }
    }

    public void listarProdutos() {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            List<Produto> produtos = new ProdutoDAO(connection).listarTodas();
            if (produtos.isEmpty()) {
                System.out.println("===== Nenhum produto cadastrado =====");
                return;
            }

            System.out.println("===== Produtos =====");
            for (Produto produto : produtos) {
                System.out.println(
                        "Código: " + produto.getCodigo()
                                + " | Nome: " + produto.getNomeDoProduto()
                                + " | Peso: " + produto.getPeso() + " kg"
                                + " | Volume: " + produto.getVolume() + " m³"
                                + " | Valor declarado: " + produto.getValor() + " R$"
                );
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao listar produtos: " + mensagemBanco(e));
        }
    }

    public Produto buscarPorCodigo(int codigo) {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new ProdutoDAO(connection).buscarPorCodigo(codigo);
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao buscar produto: " + mensagemBanco(e));
            return null;
        }
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        String texto = e.getMessage();
        if (causa != null) {
            texto = causa.getMessage();
        }
        if (texto != null && texto.contains("duplicate")) {
            return "já existe um produto com esse código";
        }
        return e.getMessage();
    }
}
