import service.Sistema;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Sistema sistema = new Sistema();

        int escolha = 1;

        while (escolha != 0) {

            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Cadastrar Produto");
            System.out.println("3 - Cadastrar Entrega");
            System.out.println("4 - Mostrar valor total da compra");
            System.out.println("5 - Iniciar Entrega");
            System.out.println("0 - Para sair");

            System.out.println("Escolha uma opção");
            escolha = input.nextInt();

            if (escolha == 1) {

                sistema.cadastrarCliente(input);

            } else if (escolha == 2) {

                sistema.cadastrarProduto(input);

            } else if (escolha == 3) {

                sistema.cadastrarEntrega(input);

            } else if (escolha == 4) {

                sistema.mostrarValorTotal(input);

            } else if (escolha == 5) {

                sistema.iniciarEntrega(input);

            } else if (escolha == 0) {

                break;
            }
        }

        input.close();
    }
}