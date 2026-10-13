package controller;

import validation.ValidadorFrete;

import java.util.List;
import java.util.Scanner;

public class SimuladorFrete {

    public void simular(Scanner input) {
        System.out.println("===== Simulador de frete =====");
        System.out.println("Não precisa cadastrar cliente, produto ou entrega");

        System.out.println("Comprimento (cm):");
        double comprimento = input.nextDouble();

        System.out.println("Altura (cm):");
        double altura = input.nextDouble();

        System.out.println("Largura (cm):");
        double largura = input.nextDouble();

        System.out.println("Peso (kg):");
        double pesoUnitario = input.nextDouble();

        System.out.println("Quantidade:");
        int quantidade = input.nextInt();
        input.nextLine();

        ValidadorFrete validadorFrete = new ValidadorFrete();
        List<String> errosFrete = validadorFrete.validar(comprimento, altura, largura, pesoUnitario, quantidade);

        if (!errosFrete.isEmpty()) {
            for (String erro : errosFrete) {
                System.out.println(erro);
            }
            System.out.println("===== Simule o frete novamente =====");
            return;
        }

        double volume = Frete.calcularVolume(comprimento, altura, largura) * quantidade;
        double peso = pesoUnitario * quantidade;
        double frete = Frete.calcular(volume, peso);

        errosFrete = validadorFrete.validarValor(frete);
        if (!errosFrete.isEmpty()) {
            for (String erro : errosFrete) {
                System.out.println(erro);
            }
            System.out.println("===== Simule o frete novamente =====");
            return;
        }

        System.out.println("Volume: " + volume + " m³");
        System.out.println("Peso: " + peso + " kg");
        System.out.println("Frete estimado: " + frete + " R$");
        System.out.println("===== Simulação concluída (nada foi gravado) =====");
    }
}
