package controller;

import model.Produto;
import validation.ValidadorProduto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaProduto {
    private List<Produto> produtos = new ArrayList<>();

    public void cadastrarProduto(Scanner input) {
        System.out.println("Nome do produto/valume:");
        String nomeDoProduto = input.nextLine();

        System.out.println("Identificador do volume:");
        int identificador = input.nextInt();

        System.out.println("Valor declarado (quanto vale a mercadoria do volume):");
        double valor = input.nextDouble();
        input.nextLine();

        Produto produto = new Produto(identificador, nomeDoProduto, valor);

        ValidadorProduto validadorProduto = new ValidadorProduto();
        List<String> errosProduto = validadorProduto.validar(produto);

        if (!errosProduto.isEmpty()) {
            for (String erro : errosProduto) {
                System.out.println(erro);
            }
            System.out.println("===== Cadastre o volume novamente =====");
        } else {
            produtos.add(produto);
            System.out.println("===== Volume cadastrado com sucesso!!! =====");
        }
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}
