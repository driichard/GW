package service;

import model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaProduto  {
    private List<Produto> produtos = new ArrayList<>();

    public void cadastrarProduto(Scanner input) {
        System.out.println("nome do produto:");
        String tipoDeProduto = input.next();

        System.out.println("id:");
        int id = input.nextInt();

        System.out.println("Valor:");
        Double valor = input.nextDouble();

        System.out.println("Quantidade:");
        int quantidade = input.nextInt();

        Produto produto = new Produto(id,
                tipoDeProduto,
                valor,
                quantidade);

        produtos.add(produto);

        System.out.println("Produto cadastrado com sucesso!!!");
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

}
