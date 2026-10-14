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

        System.out.println("Nome:");
        String nome = input.nextLine().trim();

        System.out.println("Telefone (11 números, com DDD):");
        String telefone = input.nextLine().trim();

        System.out.println("CPF ou CNPJ (somente números):");
        String documento = input.nextLine().trim();

        System.out.println("CEP (8 números):");
        String cep = input.nextLine().trim();

        System.out.println("Rua:");
        String rua = input.nextLine().trim();

        System.out.println("Número (somente números):");
        String numero = input.nextLine().trim();

        System.out.println("Bairro:");
        String bairro = input.nextLine().trim();

        System.out.println("Cidade:");
        String cidade = input.nextLine().trim();

        System.out.println("Estado (2 letras, ex: SP):");
        String estado = input.nextLine().trim();

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
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new ClienteDAO(connection).salvar(cliente);
            System.out.println("===== Cliente cadastrado com sucesso!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao gravar cliente: " + mensagemBanco(e));
        }
    }

    public void listarClientes() {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            List<Cliente> clientes = new ClienteDAO(connection).listarTodas();
            if (clientes.isEmpty()) {
                System.out.println("===== Nenhum cliente cadastrado =====");
                return;
            }

            System.out.println("===== Clientes =====");
            for (Cliente cliente : clientes) {
                System.out.println(
                        "Nome: " + cliente.getNome()
                                + " | Documento: " + cliente.getDocumento()
                                + " | Telefone: " + cliente.getTelefone()
                                + " | Endereço: " + cliente.getEndereco().getCidade()
                                + "/" + cliente.getEndereco().getEstado()
                                + " - " + cliente.getEndereco().getRua()
                                + ", " + cliente.getEndereco().getNumero()
                                + " | CEP: " + cliente.getEndereco().getCep()
                );
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao listar clientes: " + mensagemBanco(e));
        }
    }

    public void atualizarCliente(Scanner input) {
        System.out.println("CPF ou CNPJ do cliente:");
        String documento = input.nextLine().trim();
        Cliente existente = buscarPorDocumento(documento);

        if (existente == null) {
            System.out.println("===== Cliente não encontrado =====");
            return;
        }

        System.out.println("Nome:");
        String nome = input.nextLine().trim();

        System.out.println("Telefone (11 números, com DDD):");
        String telefone = input.nextLine().trim();

        System.out.println("CEP (8 números):");
        String cep = input.nextLine().trim();

        System.out.println("Rua:");
        String rua = input.nextLine().trim();

        System.out.println("Número (somente números):");
        String numero = input.nextLine().trim();

        System.out.println("Bairro:");
        String bairro = input.nextLine().trim();

        System.out.println("Cidade:");
        String cidade = input.nextLine().trim();

        System.out.println("Estado (2 letras, ex: SP):");
        String estado = input.nextLine().trim();

        Endereco endereco = new Endereco(cep, rua, numero, bairro, cidade, estado);
        Cliente cliente = new Cliente(telefone, documento, nome, endereco);

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
            System.out.println("=======Faça a alteração novamente=======");
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new ClienteDAO(connection).atualizar(cliente);
            System.out.println("===== Cliente atualizado!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao atualizar cliente: " + mensagemBanco(e));
        }
    }

    public void excluirCliente(Scanner input) {
        System.out.println("CPF ou CNPJ do cliente:");
        String documento = input.nextLine().trim();
        Cliente existente = buscarPorDocumento(documento);

        if (existente == null) {
            System.out.println("===== Cliente não encontrado =====");
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new ClienteDAO(connection).deletar(documento);
            System.out.println("===== Cliente excluído!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao excluir cliente: " + mensagemBanco(e));
        }
    }

    public Cliente buscarPorDocumento(String documento) {
        if (documento != null) {
            documento = documento.trim();
        }
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new ClienteDAO(connection).buscarPorDocumento(documento);
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao buscar cliente: " + mensagemBanco(e));
            return null;
        }
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        String texto = e.getMessage();
        if (causa != null) {
            texto = causa.getMessage();
        }
        if (texto != null && texto.contains("duplicate")) {
            return "já existe um cliente com esse CPF/CNPJ";
        }
        if (texto != null && texto.toLowerCase().contains("foreign")) {
            return "não é possível excluir: cliente vinculado a uma entrega";
        }
        return e.getMessage();
    }
}




