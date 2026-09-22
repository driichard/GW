package service;

import model.Clientes;
import model.Endereco;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaCliente {
    private List<Clientes> clientes = new ArrayList<>();

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



    public List<Clientes> getClientes() {
        return clientes;
    }
}
