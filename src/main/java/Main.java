import controller.SimuladorFrete;
import controller.SistemaCliente;
import controller.SistemaEntrega;
import controller.SistemaProduto;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        SistemaCliente sistemaCliente = new SistemaCliente();
        SistemaProduto sistemaProduto = new SistemaProduto();
        SistemaEntrega sistemaEntrega = new SistemaEntrega();
        SimuladorFrete simuladorFrete = new SimuladorFrete();

        int escolha = 1;

        while (escolha != 0) {

            System.out.println("===== Menu principal =====");
            System.out.println("1 - Cliente (remetente/destinatário)");
            System.out.println("2 - Produto (caixa da carga)");
            System.out.println("3 - Entrega");
            System.out.println("4 - Simular frete");
            System.out.println("0 - Sair");

            System.out.println("Escolha uma opção");
            escolha = lerOpcao(input);

            if (escolha == 1) {

                menuCliente(input, sistemaCliente);

            } else if (escolha == 2) {

                menuProduto(input, sistemaProduto);

            } else if (escolha == 3) {

                menuEntrega(input, sistemaCliente, sistemaProduto, sistemaEntrega);

            } else if (escolha == 4) {

                simuladorFrete.simular(input);

            } else if (escolha == 0) {

                break;
            }
        }

        input.close();
    }

    private static void menuCliente(Scanner input, SistemaCliente sistemaCliente) {
        int escolha = 1;

        while (escolha != 0) {
            System.out.println("===== Menu cliente =====");
            System.out.println("1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Atualizar cliente");
            System.out.println("4 - Excluir cliente");
            System.out.println("0 - Voltar");

            System.out.println("Escolha uma opção");
            escolha = lerOpcao(input);

            if (escolha == 1) {
                sistemaCliente.cadastrarCliente(input);
            } else if (escolha == 2) {
                sistemaCliente.listarClientes();
            } else if (escolha == 3) {
                sistemaCliente.atualizarCliente(input);
            } else if (escolha == 4) {
                sistemaCliente.excluirCliente(input);
            }
        }
    }

    private static void menuProduto(Scanner input, SistemaProduto sistemaProduto) {
        int escolha = 1;

        while (escolha != 0) {
            System.out.println("===== Menu produto =====");
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos");
            System.out.println("0 - Voltar");

            System.out.println("Escolha uma opção");
            escolha = lerOpcao(input);

            if (escolha == 1) {
                sistemaProduto.cadastrarProduto(input);
            } else if (escolha == 2) {
                sistemaProduto.listarProdutos();
            }
        }
    }

    private static void menuEntrega(Scanner input, SistemaCliente sistemaCliente, SistemaProduto sistemaProduto, SistemaEntrega sistemaEntrega) {
        int escolha = 1;

        while (escolha != 0) {
            System.out.println("===== Menu entrega =====");
            System.out.println("Status: Pendente → Em andamento → Entregue");
            System.out.println("1 - Cadastrar entrega");
            System.out.println("2 - Listar entregas");
            System.out.println("3 - Iniciar entrega");
            System.out.println("4 - Concluir entrega");
            System.out.println("5 - Cancelar entrega");
            System.out.println("6 - Excluir entrega");
            System.out.println("0 - Voltar");

            System.out.println("Escolha uma opção");
            escolha = lerOpcao(input);

            if (escolha == 1) {
                sistemaEntrega.cadastrarEntrega(input, sistemaCliente, sistemaProduto);
            } else if (escolha == 2) {
                sistemaEntrega.listarEntregas();
            } else if (escolha == 3) {
                sistemaEntrega.iniciarEntrega(input);
            } else if (escolha == 4) {
                sistemaEntrega.concluirEntrega(input);
            } else if (escolha == 5) {
                sistemaEntrega.cancelarEntrega(input);
            } else if (escolha == 6) {
                sistemaEntrega.excluirEntrega(input);
            }
        }
    }

    private static int lerOpcao(Scanner input) {
        try {
            int escolha = input.nextInt();
            input.nextLine();
            return escolha;
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida");
            input.nextLine();
            return -1;
        }
    }
}
