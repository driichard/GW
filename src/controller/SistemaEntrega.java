package service;

import model.Clientes;
import model.Entrega;
import model.Produto;
import validation.ValidadorEntrega;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaEntrega {
    private List<Entrega> entregas = new ArrayList<>();

    public void cadastrarEntrega(Scanner input, SistemaCliente clientes, SistemaProduto sistemasProduto) {
        System.out.println("ID entrega");
        int id = input.nextInt();

        System.out.println("CPF do Cliente");
        String cpf = input.next();

        Clientes clienteEncontrado = null;

        for (Clientes c : clientes.getClientes()){
            if (c.getCpf().equals(cpf)) {
                clienteEncontrado = c;
                break;
            }
        }

        if  (clienteEncontrado == null) {
            System.out.println("Cliente não encontrado");
            return;
        }

        System.out.println("ID Produto");
        int idProduto = input.nextInt();

        Produto produtoEncontrado = null;

        for (Produto p : sistemasProduto.getProdutos()){
            if (p.getId() == idProduto){
                produtoEncontrado = p;
                break;
            }
        }

        if (produtoEncontrado == null){
            System.out.println("Produto n encontrado");
            return;
        }

        System.out.println("Valor da entrega");
        double valorDaEntrega = input.nextDouble();

        input.nextLine();

        System.out.println("Status");
        String status = input.nextLine();

        Entrega entrega = new Entrega(id, clienteEncontrado, produtoEncontrado, valorDaEntrega, status);
        Valores valores = new Valores();
        ValidadorEntrega validadorEntrega = new ValidadorEntrega ();

        List<String> errosEntrega = validadorEntrega.validar(entrega);

        if (!errosEntrega.isEmpty()){
            for (String erro : errosEntrega) {
                System.out.println(erro);
            }

            System.out.println("===== Faça o cadastro novamente =====");

        }else {
            entregas.add(entrega);
            System.out.println( "Valor total da compra + frete"+ " " + valores.calcularValorTotal(produtoEncontrado, entrega)+ "R$");
          //  System.out.println(entrega.valorTotal());
            System.out.println("Entrega cadastrada!!!");
        }
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }

    public void iniciarEntrega(Scanner input) {

        System.out.println("ID da entrega:");
        int id = input.nextInt();

        for (Entrega entrega : entregas) {
            if (entrega.getId() == id) {
                entrega.setStatus("Em andamento");
                System.out.println("Entrega iniciada!");
                return;
            }
        }

        System.out.println("Entrega não encontrada.");
    }
}

