package servlet;

import dao.EntregaDAO;
import db.ConnectionFactory;
import model.Entrega;
import model.StatusEntrega;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/lista-entregas")
public class ListarEntregasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "entregas");

        String acao = request.getParameter("acao");

        if ("excluir".equals(acao)) {
            excluir(request, response);
            return;
        }

        if ("iniciar".equals(acao) || "concluir".equals(acao) || "cancelar".equals(acao)) {
            atualizar(request, response);
            return;
        }

        mostrarLista(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
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
            EntregaDAO entregaDAO = new EntregaDAO(connection);
            Entrega entrega = entregaDAO.buscarPorId(id);
            if (entrega == null) {
                request.setAttribute("erro", "Entrega não encontrada");
                mostrarLista(request, response);
                return;
            }

            if (entrega.getStatus() != StatusEntrega.ENTREGUE
                    && entrega.getStatus() != StatusEntrega.CANCELADA) {
                request.setAttribute("erro", "Só é possível excluir entrega concluída ou cancelada");
                mostrarLista(request, response);
                return;
            }

            entregaDAO.excluir(entrega.getId());
            response.sendRedirect("lista-entregas");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao excluir entrega: " + mensagemBanco(e));
            mostrarLista(request, response);
        }
    }

    private void atualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = inteiro(request, "id");
        String acao = request.getParameter("acao");
        if (id == null) {
            request.setAttribute("erro", "Id inválido");
            mostrarLista(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            EntregaDAO entregaDAO = new EntregaDAO(connection);
            Entrega entrega = entregaDAO.buscarPorId(id);
            if (entrega == null) {
                request.setAttribute("erro", "Entrega não encontrada");
                mostrarLista(request, response);
                return;
            }

            if ("iniciar".equals(acao)) {
                if (entrega.getStatus() != StatusEntrega.PENDENTE) {
                    request.setAttribute("erro", "Só é possível iniciar entrega pendente");
                    mostrarLista(request, response);
                    return;
                }
                entrega.setStatus(StatusEntrega.EM_ANDAMENTO);
                entregaDAO.atualizar(entrega);
            }

            if ("concluir".equals(acao)) {
                if (entrega.getStatus() != StatusEntrega.EM_ANDAMENTO) {
                    request.setAttribute("erro", "Só é possível concluir entrega em andamento");
                    mostrarLista(request, response);
                    return;
                }
                entrega.setStatus(StatusEntrega.ENTREGUE);
                entregaDAO.atualizar(entrega);
            }

            if ("cancelar".equals(acao)) {
                if (entrega.getStatus() == StatusEntrega.ENTREGUE) {
                    request.setAttribute("erro", "Não é possível cancelar uma entrega já concluída");
                    mostrarLista(request, response);
                    return;
                }
                if (entrega.getStatus() == StatusEntrega.CANCELADA) {
                    request.setAttribute("erro", "Entrega já está cancelada");
                    mostrarLista(request, response);
                    return;
                }
                entrega.setStatus(StatusEntrega.CANCELADA);
                entregaDAO.atualizar(entrega);
            }

            response.sendRedirect("lista-entregas");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao atualizar status: " + mensagemBanco(e));
            mostrarLista(request, response);
        }
    }

    private void mostrarLista(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            request.setAttribute("listaEntregas", new EntregaDAO(connection).listarTodas());
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao listar entregas: " + mensagemBanco(e));
        }

        request.getRequestDispatcher("lista-entregas.jsp").forward(request, response);
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
        return texto != null ? texto : e.getMessage();
    }
}
