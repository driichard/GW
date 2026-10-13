package controller;

import dao.ClienteDAO;
import db.ConnectionFactory;
import model.Cliente;
import model.Endereco;
import validation.ValidadorCliente;
import validation.ValidadorEndereco;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class SistemaCliente {

    public void cadastrarCliente(Scanner input) {

        System.out.println("Nome do remetente ou destinatário:");
        String nome = input.nextLine();

        System.out.println("Telefone (Digite com ddd): ");
        String telefone = input.nextLine();

        System.out.println("CPF ou CNPJ (Apenas números):");
        String documento = input.nextLine();

        System.out.println("CEP (Apenas números):");
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

        Cliente cliente = new Cliente(
                telefone,
                documento,
                nome,
                endereco
        );

        ValidadorCliente validadorCliente = new ValidadorCliente();
        ValidadorEndereco validadorEndereco = new ValidadorEndereco();

        List<String> erros = validadorCliente.validar(cliente);
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
            try (Connection connection = new ConnectionFactory().recuperarConexao()) {
                new ClienteDAO(connection).salvar(cliente);
                System.out.println("===== Cliente cadastrado com sucesso!!! =====");
            } catch (SQLException | RuntimeException e) {
                System.out.println("Erro ao gravar cliente: " + mensagemBanco(e));
            }
        }
    }

    public List<Cliente> getClientes() {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new ClienteDAO(connection).listarTodas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Cliente buscarPorDocumento(String documento) {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new ClienteDAO(connection).buscarPorDocumento(documento);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        if (causa != null && causa.getMessage() != null && causa.getMessage().contains("duplicate")) {
            return "já existe um cliente com esse CPF/CNPJ";
        }
        return e.getMessage();
    }
}




