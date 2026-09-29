import controller.Valores;
import controller.SistemaCliente;
import controller.SistemaEntrega;
import controller.SistemaProduto;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Valores valores = new Valores();
        SistemaCliente sistemaCliente = new SistemaCliente();
        SistemaProduto sistemaProduto = new SistemaProduto();
        SistemaEntrega sistemaEntrega = new SistemaEntrega();

        int escolha = 1;

        while (escolha != 0) {

            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Cadastrar Produto");
            System.out.println("3 - Cadastrar Entrega");
            System.out.println("4 - Iniciar Entrega");
            System.out.println("5 - Cancelar Entrega");
            System.out.println("0 - Para sair");

            System.out.println("Escolha uma opção");
            escolha = input.nextInt();
            input.nextLine();

            if (escolha == 1) {

               sistemaCliente.cadastrarCliente(input);

            } else if (escolha == 2) {

                sistemaProduto.cadastrarProduto(input);

            } else if (escolha == 3) {

                sistemaEntrega.cadastrarEntrega(input, sistemaCliente, sistemaProduto);

            } else if (escolha == 4) {

                sistemaEntrega.iniciarEntrega(input);

            } else if (escolha == 5) {

                sistemaEntrega.cancelarEntrega(input);

            } else if (escolha == 0) {

                break;
            }
        }

        input.close();
    }
}