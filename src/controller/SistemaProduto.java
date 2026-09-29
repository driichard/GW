package controller;

import model.Produto;
import validation.ValidadorProduto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaProduto {
    private List<Produto> produtos = new ArrayList<>();

    public void cadastrarProduto(Scanner input) {
        System.out.println("nome do produto:");
        String tipoDeProduto = input.next();

        System.out.println("ID produto:");
        int id = input.nextInt();

        System.out.println("Valor:");
        double valor = input.nextDouble();

        System.out.println("Quantidade:");
        int quantidade = input.nextInt();

        Produto produto = new Produto(id,
                tipoDeProduto,
                valor,
                quantidade);

        ValidadorProduto validadorProduto = new ValidadorProduto();
        List<String> errosProduto = validadorProduto.validar(produto);

        if(!errosProduto.isEmpty()) {
            for (String erro : errosProduto) {
                System.out.println(erro);
            }
            System.out.println("===== Cadastrar ptoduto novamente =====");

        } else {
            produtos.add(produto);

            System.out.printf("Valor da compra: %.2f R$%n", valor * quantidade);
            System.out.println("===== Produto cadastrado com sucesso!!! =====");
        }
    }

    public List<Produto> getProdutos() {
        return produtos;
    }
}
