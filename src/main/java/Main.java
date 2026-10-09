import controller.SistemaCliente;
import controller.SistemaEntrega;
import controller.SistemaProduto;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        SistemaCliente sistemaCliente = new SistemaCliente();
        SistemaProduto sistemaProduto = new SistemaProduto();
        SistemaEntrega sistemaEntrega = new SistemaEntrega();

        int escolha = 1;

        while (escolha != 0) {

            System.out.println("1 - Cadastrar Cliente");
            System.out.println("2 - Cadastrar produto/volume");
            System.out.println("3 - Cadastrar Entrega");
            System.out.println("4 - Iniciar Entrega");
            System.out.println("5 - Concluir Entrega");
            System.out.println("6 - Cancelar Entrega");
            System.out.println("7 - Listar Entregas");
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

                sistemaEntrega.concluirEntrega(input);

            } else if (escolha == 6) {

                sistemaEntrega.cancelarEntrega(input);

            } else if (escolha == 7) {

                sistemaEntrega.listarEntregas();

            } else if (escolha == 0) {

                break;
            }
        }

        input.close();
    }
}