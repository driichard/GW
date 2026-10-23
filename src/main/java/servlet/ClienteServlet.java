package servlet;

import dao.ClienteDAO;
import db.ConnectionFactory;
import model.Cliente;
import model.Endereco;
import validation.ValidadorCliente;
import validation.ValidadorEndereco;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/lancar-cliente")
public class ClienteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "clientes");

        if ("editar".equals(request.getParameter("acao"))) {
            editar(request, response);
            return;
        }

        request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "clientes");

        if ("salvarAlteracao".equals(request.getParameter("acao"))) {
            salvarAlteracao(request, response);
            return;
        }

        cadastrarCliente(request, response);
    }

    private void cadastrarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cliente cliente = montarCliente(request);
        ValidadorCliente validadorCliente = new ValidadorCliente();
        ValidadorEndereco validadorEndereco = new ValidadorEndereco();
        List<String> erros = new ArrayList<>();
        erros.addAll(validadorCliente.validar(cliente));
        erros.addAll(validadorEndereco.validar(cliente.getEndereco()));
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new ClienteDAO(connection).salvar(cliente);
            response.sendRedirect("lista-clientes");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao gravar cliente: " + mensagemBanco(e));
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String documento = request.getParameter("documento");

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            Cliente cliente = new ClienteDAO(connection).buscarPorDocumento(documento);
            if (cliente == null) {
                request.setAttribute("erro", "Cliente não encontrado");
                request.getRequestDispatcher("lista-clientes").forward(request, response);
                return;
            }

            request.setAttribute("cliente", cliente);
            request.setAttribute("editar", true);
            request.setAttribute("documentoAtual", cliente.getDocumento());
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao buscar cliente: " + mensagemBanco(e));
            request.getRequestDispatcher("lista-clientes").forward(request, response);
        }
    }

    private void salvarAlteracao(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String documentoAtual = request.getParameter("documentoAtual");
        Cliente cliente = montarCliente(request);
        ValidadorCliente validadorCliente = new ValidadorCliente();
        ValidadorEndereco validadorEndereco = new ValidadorEndereco();
        List<String> erros = new ArrayList<>();
        erros.addAll(validadorCliente.validar(cliente));
        erros.addAll(validadorEndereco.validar(cliente.getEndereco()));
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            request.setAttribute("editar", true);
            request.setAttribute("cliente", cliente);
            request.setAttribute("documentoAtual", documentoAtual);
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            Cliente gravado = clienteDAO.buscarPorDocumento(documentoAtual);
            if (gravado == null) {
                request.setAttribute("erro", "Cliente não encontrado");
                request.getRequestDispatcher("lista-clientes").forward(request, response);
                return;
            }

            Endereco endereco = cliente.getEndereco();
            Endereco enderecoGravado = gravado.getEndereco();
            if (endereco.getId() > 0 && (enderecoGravado == null || enderecoGravado.getId() != endereco.getId())) {
                request.setAttribute("erro", "Endereço não encontrado para este cliente");
                request.setAttribute("editar", true);
                request.setAttribute("cliente", cliente);
                request.setAttribute("documentoAtual", documentoAtual);
                request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
                return;
            }

            clienteDAO.atualizar(documentoAtual, cliente);
            response.sendRedirect("lista-clientes");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao atualizar cliente: " + mensagemBanco(e));
            request.setAttribute("editar", true);
            request.setAttribute("cliente", cliente);
            request.setAttribute("documentoAtual", documentoAtual);
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
        }
    }

    private Cliente montarCliente(HttpServletRequest request) {
        Integer idEndereco = inteiro(request.getParameter("enderecoId"));
        Endereco endereco = new Endereco(
                idEndereco == null ? 0 : idEndereco,
                texto(request, "cep"),
                texto(request, "rua"),
                texto(request, "numero"),
                texto(request, "bairro"),
                texto(request, "cidade"),
                texto(request, "estado")
        );
        return new Cliente(
                texto(request, "telefone"),
                texto(request, "documento"),
                texto(request, "nome"),
                endereco
        );
    }

    private String texto(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
        if (valor == null) {
            return "";
        }
        return valor.trim();
    }

    private Integer inteiro(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
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
        return texto != null ? texto : e.getMessage();
    }
}
