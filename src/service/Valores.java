package service;

import model.Entrega;
import model.Produto;

import java.util.Scanner;

public class Valores {
    public double calcularValorTotal (Produto produto, Entrega entrega) {
        return produto.getValor() * produto.getQuantidade() + entrega.getValorEntrega();
    }

    public void mostrarValorTotal(Scanner input, SistemaEntrega sistemaEntrega) {

        System.out.println("Digite o ID da entrega");
        int id = input.nextInt();

        Valores valores = new Valores();

        for (Entrega e : sistemaEntrega.getEntregas()){
            if (e.getId() == id ){
                System.out.println("Valor total é " + e.valorTotal(valores));
                return;
            }
        }

        System.out.println("Valor não encontrado!");
    }


}
