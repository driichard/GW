package servlet;

import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Produto;
import validation.ValidadorProduto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/lancar-produto")
public class ProdutoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "produtos");

        if ("editar".equals(request.getParameter("acao"))) {
            editar(request, response);
            return;
        }

        request.getRequestDispatcher("form-produto.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "produtos");

        if ("atualizarProduto".equals(request.getParameter("acao"))) {
            atualizarProduto(request, response);
            return;
        }

        cadastrarProduto(request, response);
    }

    private void cadastrarProduto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Produto produto = montarProduto(request, 0);
        ValidadorProduto validadorProduto = new ValidadorProduto();
        List<String> erros = validadorProduto.validar(produto);
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            request.setAttribute("produto", produto);
            request.getRequestDispatcher("form-produto.jsp").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            new ProdutoDAO(connection).salvar(produto);
            response.sendRedirect("lista-produtos");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao gravar produto: " + mensagemBanco(e));
            request.setAttribute("produto", produto);
            request.getRequestDispatcher("form-produto.jsp").forward(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = inteiro(request, "id");
        if (id == null) {
            request.setAttribute("erro", "Id inválido");
            request.getRequestDispatcher("lista-produtos").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            Produto produto = new ProdutoDAO(connection).buscarPorId(id);
            if (produto == null) {
                request.setAttribute("erro", "Produto não encontrado");
                request.getRequestDispatcher("lista-produtos").forward(request, response);
                return;
            }

            request.setAttribute("produto", produto);
            request.setAttribute("editar", true);
            request.getRequestDispatcher("form-produto.jsp").forward(request, response);
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao buscar produto: " + mensagemBanco(e));
            request.getRequestDispatcher("lista-produtos").forward(request, response);
        }
    }

    private void atualizarProduto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = inteiro(request, "id");
        if (id == null) {
            request.setAttribute("erro", "Id inválido");
            request.getRequestDispatcher("lista-produtos").forward(request, response);
            return;
        }

        Produto produto = montarProduto(request, id);
        ValidadorProduto validadorProduto = new ValidadorProduto();
        List<String> erros = validadorProduto.validar(produto);
        if (!erros.isEmpty()) {
            request.setAttribute("erros", erros);
            request.setAttribute("editar", true);
            request.setAttribute("produto", produto);
            request.getRequestDispatcher("form-produto.jsp").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            ProdutoDAO produtoDAO = new ProdutoDAO(connection);
            if (produtoDAO.buscarPorId(id) == null) {
                request.setAttribute("erro", "Produto não encontrado");
                request.getRequestDispatcher("lista-produtos").forward(request, response);
                return;
            }

            produtoDAO.atualizar(produto);
            response.sendRedirect("lista-produtos");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao atualizar produto: " + mensagemBanco(e));
            request.setAttribute("editar", true);
            request.setAttribute("produto", produto);
            request.getRequestDispatcher("form-produto.jsp").forward(request, response);
        }
    }

    private Produto montarProduto(HttpServletRequest request, int id) {
        return new Produto(
                id,
                texto(request, "codigo"),
                texto(request, "nome"),
                numeroDecimal(texto(request, "valor")),
                numeroDecimal(texto(request, "peso")),
                numeroDecimal(texto(request, "comprimento")),
                numeroDecimal(texto(request, "altura")),
                numeroDecimal(texto(request, "largura"))
        );
    }

    private String texto(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
        if (valor == null) {
            return "";
        }
        return valor.trim();
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

    private double numeroDecimal(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        String texto = e.getMessage();
        if (causa != null) {
            texto = causa.getMessage();
        }
        return texto != null ? texto : e.getMessage();
    }
}
