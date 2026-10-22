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

        request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "clientes");

        String acao = request.getParameter("acao");

        if ("editar".equals(acao)) {
            editar(request, response);
            return;
        }

        if ("salvarAlteracao".equals(acao)) {
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
        List<Endereco> enderecos = lerEnderecosExistentes(request);
        Cliente cliente = new Cliente(
                texto(request, "telefone"),
                texto(request, "documento"),
                texto(request, "nome"),
                enderecos
        );
        Endereco novoEndereco = lerNovoEndereco(request);

        ValidadorCliente validadorCliente = new ValidadorCliente();
        ValidadorEndereco validadorEndereco = new ValidadorEndereco();
        List<String> erros = new ArrayList<>();
        erros.addAll(validadorCliente.validar(cliente));
        for (Endereco endereco : enderecos) {
            erros.addAll(validadorEndereco.validar(endereco));
        }
        if (novoEndereco != null) {
            erros.addAll(validadorEndereco.validar(novoEndereco));
        }

        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            request.setAttribute("editar", true);
            request.setAttribute("cliente", cliente);
            request.setAttribute("documentoAtual", documentoAtual);
            request.setAttribute("novoEndereco", novoEndereco);
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            if (clienteDAO.buscarPorDocumento(documentoAtual) == null) {
                request.setAttribute("erro", "Cliente não encontrado");
                request.getRequestDispatcher("lista-clientes").forward(request, response);
                return;
            }

            clienteDAO.atualizar(documentoAtual, cliente);
            if (novoEndereco != null) {
                clienteDAO.adicionarEndereco(cliente.getDocumento(), novoEndereco);
            }
            response.sendRedirect("lista-clientes");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao atualizar cliente: " + mensagemBanco(e));
            request.setAttribute("editar", true);
            request.setAttribute("cliente", cliente);
            request.setAttribute("documentoAtual", documentoAtual);
            request.setAttribute("novoEndereco", novoEndereco);
            request.getRequestDispatcher("form-cliente.jsp").forward(request, response);
        }
    }

    private List<Endereco> lerEnderecosExistentes(HttpServletRequest request) {
        List<Endereco> enderecos = new ArrayList<>();
        String[] ids = request.getParameterValues("enderecoId");
        if (ids == null) {
            return enderecos;
        }

        for (String idTexto : ids) {
            Integer id = inteiro(idTexto);
            if (id == null) {
                continue;
            }
            enderecos.add(new Endereco(
                    id,
                    texto(request, "cep_" + id),
                    texto(request, "rua_" + id),
                    texto(request, "numero_" + id),
                    texto(request, "bairro_" + id),
                    texto(request, "cidade_" + id),
                    texto(request, "estado_" + id)
            ));
        }
        return enderecos;
    }

    private Endereco lerNovoEndereco(HttpServletRequest request) {
        if (texto(request, "cep").isEmpty()
                && texto(request, "rua").isEmpty()
                && texto(request, "numero").isEmpty()
                && texto(request, "bairro").isEmpty()
                && texto(request, "cidade").isEmpty()
                && texto(request, "estado").isEmpty()) {
            return null;
        }

        return new Endereco(
                0,
                texto(request, "cep"),
                texto(request, "rua"),
                texto(request, "numero"),
                texto(request, "bairro"),
                texto(request, "cidade"),
                texto(request, "estado")
        );
    }

    private Cliente montarCliente(HttpServletRequest request) {
        Endereco endereco = new Endereco(
                0,
                texto(request, "cep"),
                texto(request, "rua"),
                texto(request, "numero"),
                texto(request, "bairro"),
                texto(request, "cidade"),
                texto(request, "estado")
        );
        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(endereco);
        return new Cliente(
                texto(request, "telefone"),
                texto(request, "documento"),
                texto(request, "nome"),
                enderecos
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
