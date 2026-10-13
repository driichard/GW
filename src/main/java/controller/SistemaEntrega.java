package controller;

import dao.EntregaDAO;
import db.ConnectionFactory;
import model.Cliente;
import model.Entrega;
import model.ItemEntrega;
import model.Produto;
import model.StatusEntrega;
import validation.ValidadorEntrega;
import validation.ValidadorFrete;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaEntrega {

    public void cadastrarEntrega(Scanner input, SistemaCliente clientes, SistemaProduto sistemasProduto) {
        System.out.println("Código da entrega:");
        int codigo = input.nextInt();
        input.nextLine();

        System.out.println("CPF ou CNPJ do remetente:");
        String documentoRemetente = input.nextLine();
        Cliente remetente = clientes.buscarPorDocumento(documentoRemetente);

        if (remetente == null) {
            System.out.println("Remetente não encontrado");
            return;
        }

        System.out.println("CPF ou CNPJ do destinatário:");
        String documentoDestinatario = input.nextLine();
        Cliente destinatario = clientes.buscarPorDocumento(documentoDestinatario);

        if (destinatario == null) {
            System.out.println("Destinatário não encontrado");
            return;
        }

        if (documentoRemetente.equals(documentoDestinatario)) {
            System.out.println("Remetente e destinatário precisam ser diferentes");
            return;
        }

        List<ItemEntrega> itens = new ArrayList<>();
        String continuar = "s";

        while (continuar.equals("s")) {
            System.out.println("Código do produto:");

            int codigoProduto = input.nextInt();
            input.nextLine();

            Produto produtoEncontrado = sistemasProduto.buscarPorCodigo(codigoProduto);

            if (produtoEncontrado == null) {
                System.out.println("Produto não encontrado");
            } else {
                boolean jaExiste = false;

                for (ItemEntrega itemNaLista : itens) {
                    if (itemNaLista.getProduto().getCodigo() == codigoProduto) {
                        jaExiste = true;
                        break;
                    }
                }

                if (jaExiste) {
                    System.out.println("Esse produto já está na entrega");
                } else {
                    System.out.println("Quantidade:");
                    int quantidadeItem = input.nextInt();
                    input.nextLine();

                    ItemEntrega item = new ItemEntrega(produtoEncontrado, quantidadeItem);
                    itens.add(item);
                }
            }

            System.out.println("Adicionar outro produto? (s/n)");
            continuar = input.nextLine().trim().toLowerCase();
        }

        ValidadorFrete validadorFrete = new ValidadorFrete();
        List<String> errosFrete = validadorFrete.validar(itens);

        if (!errosFrete.isEmpty()) {
            for (String erro : errosFrete) {
                System.out.println(erro);
            }
            System.out.println("===== Faça o cadastro novamente!!! =====");
            return;
        }

        double valorEntrega = Frete.calcular(itens);
        errosFrete = validadorFrete.validarValor(valorEntrega);

        Entrega entrega = new Entrega(codigo, remetente, destinatario, itens, valorEntrega, StatusEntrega.PENDENTE);
        ValidadorEntrega validadorEntrega = new ValidadorEntrega();

        List<String> errosEntrega = validadorEntrega.validar(entrega);
        errosEntrega.addAll(errosFrete);

        if (!errosEntrega.isEmpty()) {
            for (String erro : errosEntrega) {
                System.out.println(erro);
            }
            System.out.println("===== Faça o cadastro novamente!!! =====");
        } else {
            try (Connection connection = new ConnectionFactory().recuperarConexao()) {
                new EntregaDAO(connection).salvar(entrega);
            } catch (SQLException | RuntimeException e) {
                System.out.println("Erro ao gravar entrega: " + mensagemBanco(e));
                return;
            }

            System.out.println("Origem: " + enderecoTexto(remetente));
            System.out.println("Destino: " + enderecoTexto(destinatario));

            for (ItemEntrega item : itens) {
                System.out.println(
                        item.getProduto().getNomeDoProduto()
                                + " | Peso: " + item.getProduto().getPeso() + " kg"
                                + " | Volume: " + item.getProduto().getVolume() + " m³"
                                + " | Valor declarado: " + item.getValorUnitario() + " R$"
                );
            }

            double volumeTotal = 0;
            double pesoTotal = 0;
            for (ItemEntrega item : itens) {
                volumeTotal += item.getProduto().getVolume() * item.getQuantidade();
                pesoTotal += item.getProduto().getPeso() * item.getQuantidade();
            }

            System.out.println("Volume total: " + volumeTotal + " m³");
            System.out.println("Peso total: " + pesoTotal + " kg");
            System.out.println("Frete a pagar: " + entrega.getValorEntrega() + " R$");
            System.out.println("Status: " + entrega.getStatus());
            System.out.println("===== Entrega cadastrada!!! =====");
        }
    }

    public List<Entrega> getEntregas() {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            return new EntregaDAO(connection).listarTodas();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void listarEntregas() {
        List<Entrega> entregas = getEntregas();
        if (entregas.isEmpty()) {
            System.out.println("===== Nenhuma entrega cadastrada =====");
            return;
        }

        System.out.println("===== Entregas =====");
        for (Entrega entrega : entregas) {
            System.out.println(
                    "Código: " + entrega.getCodigo()
                            + " | Remetente: " + entrega.getRemetente().getNome()
                            + " | Destinatário: " + entrega.getDestinatario().getNome()
                            + " | Origem: " + entrega.getRemetente().getEndereco().getCidade()
                            + "/" + entrega.getRemetente().getEndereco().getEstado()
                            + " → Destino: " + entrega.getDestinatario().getEndereco().getCidade()
                            + "/" + entrega.getDestinatario().getEndereco().getEstado()
                            + " | Status: " + entrega.getStatus()
                            + " | Frete: R$ " + entrega.getValorEntrega()
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

        atualizarStatus(entrega, StatusEntrega.EM_ANDAMENTO);
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

        atualizarStatus(entrega, StatusEntrega.ENTREGUE);
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

        atualizarStatus(entrega, StatusEntrega.CANCELADA);
        System.out.println("Status: " + entrega.getStatus());
        System.out.println("===== Entrega cancelada!!! =====");
    }

    private Entrega buscarEntrega(Scanner input) {
        System.out.println("Código da entrega:");
        int codigo = input.nextInt();
        input.nextLine();

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            Entrega entrega = new EntregaDAO(connection).buscarPorCodigo(codigo);
            if (entrega == null) {
                System.out.println("===== Entrega não encontrada!!! =====");
            }
            return entrega;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void atualizarStatus(Entrega entrega, StatusEntrega status) {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            entrega.setStatus(status);
            new EntregaDAO(connection).atualizar(entrega);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String enderecoTexto(Cliente cliente) {
        return cliente.getNome()
                + " - "
                + cliente.getEndereco().getCidade()
                + "/"
                + cliente.getEndereco().getEstado();
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        if (causa != null && causa.getMessage() != null && causa.getMessage().contains("duplicate")) {
            return "já existe uma entrega com esse código";
        }
        return e.getMessage();
    }
}
