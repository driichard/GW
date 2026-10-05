package controller;

import model.*;
import validation.ValidadorEntrega;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaEntrega {
    private List<Entrega> entregas = new ArrayList<>();

    public void cadastrarEntrega(Scanner input, SistemaCliente clientes, SistemaProduto sistemasProduto) {
        System.out.println("Identificador da entrega");
        int identificador = input.nextInt();
        input.nextLine();

        System.out.println("CPF ou CNPJ do Cliente");
        String documento = input.nextLine();

        Clientes clienteEncontrado = null;

        for (Clientes c : clientes.getClientes()){
            if (c.getDocumento().equals(documento)) {
                clienteEncontrado = c;
                break;
            }
        }

        if  (clienteEncontrado == null) {
            System.out.println("Cliente não encontrado");
            return;
        }

        System.out.println("Identificador do volume");
        int identificadorProduto = input.nextInt();

        Produto produtoEncontrado = null;

        for (Produto p : sistemasProduto.getProdutos()){
            if (p.getIdentificador() == identificadorProduto){
                produtoEncontrado = p;
                break;
            }
        }

        if (produtoEncontrado == null){
            System.out.println("Volume não encontrado");
            return;
        }

        System.out.println("Quantidade de volumes");
        int quantidadeItem = input.nextInt();
        input.nextLine();

        ItemEntrega item = new ItemEntrega(produtoEncontrado, quantidadeItem);

        List<ItemEntrega> itens = new ArrayList<>();
        itens.add(item);

        double valorEntrega = Frete.calculular(itens);

        Entrega entrega = new Entrega(identificador, clienteEncontrado, itens , valorEntrega, StatusEntrega.PENDENTE);

        ValidadorEntrega validadorEntrega = new ValidadorEntrega ();

        List<String> errosEntrega = validadorEntrega.validar(entrega);

        if (!errosEntrega.isEmpty()) {
            for (String erro : errosEntrega) {
                System.out.println(erro);
            }
            System.out.println("===== Faça o cadastro novamente!!! =====");
        } else {
            entregas.add(entrega);
            System.out.println("Valor declarado: " + produtoEncontrado.getValor() + " R$ por volume");
            System.out.println("Frete a pagar: " + entrega.getValorEntrega() + " R$");
            System.out.println("Status: " + entrega.getStatus());
            System.out.println("===== Entrega cadastrada!!! =====");
        }
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }

    public void listarEntregas() {
        if (entregas.isEmpty()) {
            System.out.println("===== Nenhuma entrega cadastrada =====");
            return;
        }

        System.out.println("===== Entregas =====");

        for (Entrega entrega : entregas) {
            System.out.println(
                    "Identificador: " + entrega.getIdentificador()
                            + " | Cliente: " + entrega.getCliente().getNome()
                            + " (" + entrega.getCliente().getDocumento() + ")"
                            + " | Destino: " + entrega.getCliente().getEndereco().getCidade()
                            + "/" + entrega.getCliente().getEndereco().getEstado()
                            + " | Status: " + entrega.getStatus()
                            + " | Frete: " + entrega.getValorEntrega() + " R$"
            );
        }
    }

    public void iniciarEntrega(Scanner input) {
        Entrega entrega = buscarEntrega(input);
        if (entrega == null) {
            return;
        }

        if (entrega.getStatus() == StatusEntrega.CANCELADA) {
            System.out.println("===== Não é possível iniciar uma entrega cancelada =====");
            return;
        }

        if (entrega.getStatus() == StatusEntrega.ENTREGUE) {
            System.out.println("===== Não é possível iniciar uma entrega já concluída =====");
            return;
        }

        if (entrega.getStatus() == StatusEntrega.EM_ANDAMENTO) {
            System.out.println("===== Entrega já está em andamento =====");
            return;
        }

        entrega.setStatus(StatusEntrega.EM_ANDAMENTO);
        System.out.println("Status: " + entrega.getStatus());
        System.out.println("===== Entrega iniciada!!! =====");
    }

    public void concluirEntrega(Scanner input) {
        Entrega entrega = buscarEntrega(input);
        if (entrega == null) {
            return;
        }

        if (entrega.getStatus() != StatusEntrega.EM_ANDAMENTO) {
            System.out.println("===== Só é possível concluir entrega em andamento =====");
            return;
        }

        entrega.setStatus(StatusEntrega.ENTREGUE);
        System.out.println("Status: " + entrega.getStatus());
        System.out.println("===== Entrega concluída!!! =====");
    }

    public void cancelarEntrega(Scanner input) {
        Entrega entrega = buscarEntrega(input);
        if (entrega == null) {
            return;
        }

        if (entrega.getStatus() == StatusEntrega.ENTREGUE) {
            System.out.println("===== Não é possível cancelar uma entrega já concluída =====");
            return;
        }

        if (entrega.getStatus() == StatusEntrega.CANCELADA) {
            System.out.println("===== Entrega já está cancelada =====");
            return;
        }

        entrega.setStatus(StatusEntrega.CANCELADA);
        System.out.println("Status: " + entrega.getStatus());
        System.out.println("===== Entrega cancelada!!! =====");
    }

    private Entrega buscarEntrega(Scanner input) {
        System.out.println("Identificador da entrega:");
        int identificador = input.nextInt();
        input.nextLine();

        for (Entrega entrega : entregas) {
            if (entrega.getIdentificador() == identificador) {
                return entrega;
            }
        }

        System.out.println("===== Entrega não encontrada!!! =====");
        return null;
    }
}
