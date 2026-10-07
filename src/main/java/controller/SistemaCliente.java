package main.java.controller;

import main.java.model.Clientes;
import main.java.model.Endereco;
//import validation.ErrosValidacoes;
import validation.ValidadorCliente;
import validation.ValidadorEndereco;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaCliente {
    private List<Clientes> clientes = new ArrayList<>();

    public void cadastrarCliente(Scanner input) {

        System.out.println("Nome:");
        String nome = input.nextLine();

        System.out.println("Telefone (Digite com ddd): ");
        String telefone = input.nextLine();

        System.out.println("CPF ou CNPJ (Apenas número):");
        String documento = input.nextLine();

        System.out.println("CEP (Apenas número):");
        String cep = input.nextLine();

        System.out.println("Rua:");
        String rua = input.nextLine();

        System.out.println("Número:");
        String numero = input.nextLine();

        System.out.println("Bairro:");
        String bairro = input.nextLine();

        System.out.println("Cidade:");
        String cidade = input.nextLine();

        System.out.println("Estado:");
        String estado = input.nextLine();

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
                documento,
                nome,
                endereco
        );

        ValidadorCliente validadorCliente = new ValidadorCliente();
        ValidadorEndereco validadorEndereco = new ValidadorEndereco();

        List<String> erros = validadorCliente.validar(clientes);
        List<String> errosEndereco = validadorEndereco.validar(endereco);

        if (!erros.isEmpty() || !errosEndereco.isEmpty()) {
            for (String erro : erros) {
                System.out.println(erro);
            }
            for (String erro : errosEndereco) {
                System.out.println(erro);
            }
            System.out.println("=======Faça o cadastro novamente=======");
        } else {
            this.clientes.add(clientes);
            System.out.println("===== Cliente cadastrado com sucesso!!! =====");
        }
    }

    public List<Clientes> getClientes() {
        return clientes;
    }
}




