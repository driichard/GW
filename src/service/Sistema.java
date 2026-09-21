package service;

import model.Clientes;
import model.Endereco;
import model.Entrega;
import model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sistema {

    private List<Clientes> clientes = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private List<Entrega> entregas = new ArrayList<>();

    public void cadastrarCliente(Scanner input) {
        System.out.println("Nome:");
        String nome = input.nextLine();
        input.next();

        System.out.println("Telefone:");
        String telefone = input.next();

        System.out.println("CPF:");
        String cpf = input.next();

        System.out.println("CEP:");
        String cep = input.next();

        System.out.println("Rua:");
        String rua = input.next();
        input.next();

        System.out.println("Número:");
        String numero = input.next();

        System.out.println("Bairro:");
        String bairro = input.nextLine();
        input.next();

        System.out.println("Cidade:");
        String cidade = input.nextLine();
        input.next();

        System.out.println("Estado:");
        String estado = input.next();

        Endereco endereco = new Endereco(
                cep,
                rua,
                numero,
                bairro,
                cidade,
                estado
        );

        Clientes clientes = new Clientes(
                telefone,
                cpf,
                nome,
                endereco
        );

        this.clientes.add(clientes);

        System.out.println("Cliente cadastrado com sucesso!!!");
    }

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

    public void cadastrarEntrega(Scanner input) {
        System.out.println("ID");
        int id = input.nextInt();

        System.out.println("CPF do Cliente");
        String cpf = input.next();

        Clientes clienteEncontrado = null;

        for (Clientes c : clientes){
           if (c.getCpf().equals(cpf)) {
               clienteEncontrado = c;
               break;
           }
        }

        if  (clienteEncontrado == null) {
            System.out.println("Cliente não encontrado");
            return;
        }

        System.out.println("IDProduto");
        int idProduto = input.nextInt();

        Produto produtoEncontrado = null;

        for (Produto p : produtos){
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

        System.out.println("Status");
        String status = input.next();

        Entrega entrega = new Entrega(id, clienteEncontrado, produtoEncontrado, valorDaEntrega, status);

        entregas.add(entrega);

        System.out.println("Entrega cadastrada!!!");
    }

    public void mostrarValorTotal(Scanner input) {

        System.out.println("Digite o ID da entrega");
        int id = input.nextInt();

        for (Entrega e : entregas){
            if (e.getId() == id ){
                System.out.println("Valor total é " + e.valorTotal(e.getProduto()));
                return;
            }
        }

        System.out.println("Valor não encontrado!");
    }

    public void iniciarEntrega(Scanner input) {

        System.out.println("ID da entrega:");
        int id = input.nextInt();

        for (Entrega entrega : entregas) {

            if (entrega.getId() == id) {
                entrega.iniciarEntrega();
                System.out.println("Entrega iniciada!");
                return;
            }
        }

        System.out.println("Entrega não encontrada.");
    }

    public List<Clientes> getClientes() {
        return clientes;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public List<Entrega> getEntregas() {
        return entregas;
    }
}