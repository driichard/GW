package servlet;

import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Produto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/lista-produtos")
public class ListarProdutosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "produtos");
        mostrarLista(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "produtos");

        if ("excluir".equals(request.getParameter("acao"))) {
            excluir(request, response);
            return;
        }

        mostrarLista(request, response);
    }

    private void excluir(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = inteiro(request, "id");
        if (id == null) {
            request.setAttribute("erro", "Id inválido");
            mostrarLista(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            Produto existente = produtoDAO.buscarPorId(id);
            if (existente == null) {
                request.setAttribute("erro", "Produto não encontrado");
                mostrarLista(request, response);
                return;
            }

            produtoDAO.deletar(id);
            response.sendRedirect("lista-produtos");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao excluir produto: " + mensagemBanco(e));
            mostrarLista(request, response);
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            request.setAttribute("listaProdutos", new ProdutoDAO(connection).listarTodas());
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao listar produtos: " + mensagemBanco(e));
        }

        request.getRequestDispatcher("lista-produtos.jsp").forward(request, response);
    }

    private Integer inteiro(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
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
        if (texto != null && texto.toLowerCase().contains("foreign")) {
            return "não é possível excluir: produto vinculado a uma entrega";
        }
        return texto != null ? texto : e.getMessage();
    }
}
