package servlet;

import dao.ClienteDAO;
import db.ConnectionFactory;
import model.Cliente;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/lista-clientes")
public class ListarClientesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "clientes");
        mostrarLista(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "clientes");

        if ("excluir".equals(request.getParameter("acao"))) {
            excluir(request, response);
            return;
        }

        mostrarLista(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String documento = request.getParameter("documento");

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            Cliente existente = clienteDAO.buscarPorDocumento(documento);
            if (existente == null) {
                request.setAttribute("erro", "Cliente não encontrado");
                mostrarLista(request, response);
                return;
            }

            clienteDAO.deletar(documento);
            response.sendRedirect("lista-clientes");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao excluir cliente: " + mensagemBanco(e));
            mostrarLista(request, response);
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            request.setAttribute("listaClientes", new ClienteDAO(connection).listarTodas());
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao listar clientes: " + mensagemBanco(e));
        }

        request.getRequestDispatcher("lista-clientes.jsp").forward(request, response);
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        String texto = e.getMessage();
        if (causa != null) {
            texto = causa.getMessage();
        }
        if (texto != null && texto.toLowerCase().contains("foreign")) {
            return "não é possível excluir: cliente vinculado a uma entrega";
        }
        return texto != null ? texto : e.getMessage();
    }
}
