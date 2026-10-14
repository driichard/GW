package controller;

import dao.EntregaDAO;
import db.ConnectionFactory;
import model.Cliente;
import model.Entrega;
import model.ItemEntrega;
import model.Produto;
import model.StatusEntrega;
import validation.ValidadorEntrega;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SistemaEntrega {

    public void cadastrarEntrega(Scanner input, SistemaCliente clientes, SistemaProduto sistemasProduto) {
        System.out.println("Código da entrega (você escolhe o número):");
        int codigo = input.nextInt();
        input.nextLine();

        System.out.println("CPF ou CNPJ do remetente:");
        String documentoRemetente = input.nextLine().trim();
        Cliente remetente = clientes.buscarPorDocumento(documentoRemetente);

        if (remetente == null) {
            System.out.println("Remetente não encontrado. Cadastre o cliente antes.");
            return;
        }

        System.out.println("CPF ou CNPJ do destinatário:");
        String documentoDestinatario = input.nextLine().trim();
        Cliente destinatario = clientes.buscarPorDocumento(documentoDestinatario);

        if (destinatario == null) {
            System.out.println("Destinatário não encontrado. Cadastre o cliente antes.");
            return;
        }

        if (remetente.getDocumento().equals(destinatario.getDocumento())) {
            System.out.println("Remetente e destinatário precisam ser diferentes");
            return;
        }

        List<ItemEntrega> itens = new ArrayList<>();
        String continuar = "s";

        while (continuar.equals("s")) {
            System.out.println("Código da caixa:");
            int codigoProduto = input.nextInt();
            input.nextLine();

            Produto produtoEncontrado = sistemasProduto.buscarPorCodigo(codigoProduto);

            if (produtoEncontrado == null) {
                System.out.println("Caixa não encontrada. Cadastre o produto antes.");
            } else {
                boolean jaExiste = false;

                for (ItemEntrega itemNaLista : itens) {
                    if (itemNaLista.getProduto().getCodigo() == codigoProduto) {
                        jaExiste = true;
                        break;
                    }
                }

                if (jaExiste) {
                    System.out.println("Essa caixa já está na entrega");
                } else {
                    System.out.println(
                            "Caixa: " + produtoEncontrado.getNomeDoProduto()
                                    + " | Peso: " + produtoEncontrado.getPeso() + " kg"
                                    + " | Volume: " + produtoEncontrado.getVolume() + " m³"
                    );
                    System.out.println("Quantidade de caixas:");
                    int quantidadeItem = input.nextInt();
                    input.nextLine();

                    if (quantidadeItem <= 0) {
                        System.out.println("Quantidade inválida");
                    } else {
                        ItemEntrega item = new ItemEntrega(produtoEncontrado, quantidadeItem);
                        itens.add(item);
                    }
                }
            }

            System.out.println("Adicionar outra caixa? (s/n)");
            continuar = input.nextLine().trim().toLowerCase();
        }

        if (itens.isEmpty()) {
            System.out.println("Entrega sem produtos");
            System.out.println("===== Faça o cadastro novamente!!! =====");
            return;
        }

        double valorEntrega = Frete.calcular(itens);

        Entrega entrega = new Entrega(codigo, remetente, destinatario, itens, valorEntrega, StatusEntrega.PENDENTE);
        ValidadorEntrega validadorEntrega = new ValidadorEntrega();

        List<String> errosEntrega = validadorEntrega.validar(entrega);

        if (!errosEntrega.isEmpty()) {
            for (String erro : errosEntrega) {
                System.out.println(erro);
            }
            System.out.println("===== Faça o cadastro novamente!!! =====");
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new EntregaDAO(connection).salvar(entrega);
            System.out.println("Origem: " + enderecoTexto(remetente));
            System.out.println("Destino: " + enderecoTexto(destinatario));

            for (ItemEntrega item : itens) {
                System.out.println(textoCaixa(item));
            }

            System.out.println("Volume total: " + Frete.volumeTotal(itens) + " m³");
            System.out.println("Peso total: " + Frete.pesoTotal(itens) + " kg");
            System.out.println("Frete a pagar: " + entrega.getValorEntrega() + " R$");
            System.out.println("Status: " + entrega.getStatus());
            System.out.println("===== Entrega cadastrada!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao gravar entrega: " + mensagemBanco(e));
        }
    }

    public void listarEntregas() {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            List<Entrega> entregas = new EntregaDAO(connection).listarTodas();
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
                for (ItemEntrega item : entrega.getItens()) {
                    System.out.println("  " + textoCaixa(item));
                }
            }
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao listar entregas: " + mensagemBanco(e));
        }
    }

    public void excluirEntrega(Scanner input) {
        Entrega entrega = buscarEntrega(input);
        if (entrega == null) {
            return;
        }

        if (entrega.getStatus() == StatusEntrega.ENTREGUE) {
            System.out.println("===== Não é possível excluir uma entrega já concluída =====");
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new EntregaDAO(connection).deletar(entrega.getCodigo());
            System.out.println("===== Entrega excluída!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao excluir entrega: " + mensagemBanco(e));
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

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            entrega.setStatus(StatusEntrega.EM_ANDAMENTO);
            new EntregaDAO(connection).atualizar(entrega);
            System.out.println("Status: " + entrega.getStatus());
            System.out.println("===== Entrega iniciada!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao atualizar entrega: " + mensagemBanco(e));
        }
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

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            entrega.setStatus(StatusEntrega.ENTREGUE);
            new EntregaDAO(connection).atualizar(entrega);
            System.out.println("Status: " + entrega.getStatus());
            System.out.println("===== Entrega concluída!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao atualizar entrega: " + mensagemBanco(e));
        }
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

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            entrega.setStatus(StatusEntrega.CANCELADA);
            new EntregaDAO(connection).atualizar(entrega);
            System.out.println("Status: " + entrega.getStatus());
            System.out.println("===== Entrega cancelada!!! =====");
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao atualizar entrega: " + mensagemBanco(e));
        }
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
        } catch (SQLException | RuntimeException e) {
            System.out.println("Erro ao buscar entrega: " + mensagemBanco(e));
            return null;
        }
    }

    private String textoCaixa(ItemEntrega item) {
        return item.getQuantidade() + "x "
                + item.getProduto().getNomeDoProduto()
                + " | Peso: " + item.getProduto().getPeso() + " kg cada"
                + " | Volume: " + item.getProduto().getVolume() + " m³ cada"
                + " | Valor declarado: " + item.getValorUnitario() + " R$";
    }

    private String enderecoTexto(Cliente cliente) {
        return cliente.getNome()
                + " - "
                + cliente.getEndereco().getRua()
                + ", "
                + cliente.getEndereco().getNumero()
                + " - "
                + cliente.getEndereco().getCidade()
                + "/"
                + cliente.getEndereco().getEstado();
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        String texto = e.getMessage();
        if (causa != null) {
            texto = causa.getMessage();
        }
        if (texto != null && texto.contains("duplicate")) {
            return "já existe uma entrega com esse código";
        }
        return e.getMessage();
    }
}
