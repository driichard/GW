package servlet;

import dao.ClienteDAO;
import dao.EntregaDAO;
import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Cliente;
import model.Entrega;
import model.ItemEntrega;
import model.Produto;
import model.StatusEntrega;
import validation.ValidadorEntrega;

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

@WebServlet("/lancar-entrega")
public class EntregaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "entregas");

        if ("editar".equals(request.getParameter("acao"))) {
            editar(request, response);
            return;
        }

        if (!"s".equals(request.getParameter("continuar"))) {
            request.getSession().removeAttribute("itensEntrega");
            request.getSession().removeAttribute("formEntregaRemetente");
            request.getSession().removeAttribute("formEntregaDestinatario");
            request.getSession().removeAttribute("entregaEdicaoId");
        }

        mostrarFormulario(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        request.setAttribute("menuAtivo", "entregas");
        request.setAttribute("documentoRemetente", texto(request, "documentoRemetente"));
        request.setAttribute("documentoDestinatario", texto(request, "documentoDestinatario"));
        request.getSession().setAttribute("formEntregaRemetente", texto(request, "documentoRemetente"));
        request.getSession().setAttribute("formEntregaDestinatario", texto(request, "documentoDestinatario"));

        String acao = request.getParameter("acao");

        if ("adicionar".equals(acao)) {
            adicionarProduto(request, response);
            return;
        }

        if ("remover".equals(acao)) {
            removerProduto(request, response);
            return;
        }

        cadastrarEntrega(request, response);
    }

    private void cadastrarEntrega(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String documentoRemetente = texto(request, "documentoRemetente");
        String documentoDestinatario = texto(request, "documentoDestinatario");
        List<ItemEntrega> itens = itensDaSessao(request);

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            ClienteDAO clienteDAO = new ClienteDAO(connection);
            EntregaDAO entregaDAO = new EntregaDAO(connection);

            Cliente remetente = clienteDAO.buscarPorDocumento(documentoRemetente);
            Cliente destinatario = clienteDAO.buscarPorDocumento(documentoDestinatario);

            if (remetente == null) {
                request.setAttribute("erro", "Remetente não encontrado. Cadastre o cliente antes.");
                mostrarFormulario(request, response);
                return;
            }

            if (destinatario == null) {
                request.setAttribute("erro", "Destinatário não encontrado. Cadastre o cliente antes.");
                mostrarFormulario(request, response);
                return;
            }

            Integer idEdicao = idEdicao(request);
            Entrega entrega = new Entrega(
                    idEdicao == null ? 0 : idEdicao,
                    remetente,
                    destinatario,
                    remetente.getEndereco(),
                    destinatario.getEndereco(),
                    itens,
                    StatusEntrega.PENDENTE
            );

            ValidadorEntrega validadorEntrega = new ValidadorEntrega();
            List<String> erros = validadorEntrega.validar(entrega);
            if (!erros.isEmpty()) {
                request.setAttribute("erros", erros);
                mostrarFormulario(request, response);
                return;
            }

            if (idEdicao == null) {
                entregaDAO.salvar(entrega);
            } else {
                Entrega antiga = entregaDAO.buscarPorId(idEdicao);
                if (antiga == null) {
                    request.setAttribute("erro", "Entrega não encontrada");
                    mostrarFormulario(request, response);
                    return;
                }
                if (antiga.getStatus() != StatusEntrega.PENDENTE) {
                    request.setAttribute("erro", "Só é possível editar entrega pendente");
                    mostrarFormulario(request, response);
                    return;
                }
                entregaDAO.atualizarEntrega(entrega);
            }

            request.getSession().removeAttribute("itensEntrega");
            request.getSession().removeAttribute("formEntregaRemetente");
            request.getSession().removeAttribute("formEntregaDestinatario");
            request.getSession().removeAttribute("entregaEdicaoId");
            response.sendRedirect("lista-entregas");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao gravar entrega: " + mensagemBanco(e));
            mostrarFormulario(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = inteiro(request, "id");
        if (id == null) {
            request.setAttribute("erro", "Id inválido");
            request.getRequestDispatcher("lista-entregas").forward(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            Entrega entrega = new EntregaDAO(connection).buscarPorId(id);
            if (entrega == null) {
                request.setAttribute("erro", "Entrega não encontrada");
                request.getRequestDispatcher("lista-entregas").forward(request, response);
                return;
            }
            if (entrega.getStatus() != StatusEntrega.PENDENTE) {
                request.setAttribute("erro", "Só é possível editar entrega pendente");
                request.getRequestDispatcher("lista-entregas").forward(request, response);
                return;
            }

            request.getSession().setAttribute("entregaEdicaoId", entrega.getId());
            request.getSession().setAttribute("itensEntrega", new ArrayList<>(entrega.getItens()));
            request.getSession().setAttribute("formEntregaRemetente", entrega.getRemetente().getDocumento());
            request.getSession().setAttribute("formEntregaDestinatario", entrega.getDestinatario().getDocumento());
            response.sendRedirect("lancar-entrega?continuar=s");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao buscar entrega: " + mensagemBanco(e));
            request.getRequestDispatcher("lista-entregas").forward(request, response);
        }
    }

    private Integer idEdicao(HttpServletRequest request) {
        Object valor = request.getSession().getAttribute("entregaEdicaoId");
        if (valor instanceof Integer) {
            return (Integer) valor;
        }
        return null;
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getAttribute("documentoRemetente") == null) {
            request.setAttribute("documentoRemetente", textoSessao(request, "formEntregaRemetente"));
            request.setAttribute("documentoDestinatario", textoSessao(request, "formEntregaDestinatario"));
        }

        Integer idEdicao = idEdicao(request);
        if (idEdicao != null) {
            request.setAttribute("editar", true);
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            List<Cliente> clientes = new ClienteDAO(connection).listarTodas();
            request.setAttribute("listaClientes", clientes);

            List<Produto> produtos = new ProdutoDAO(connection).listarTodas(); 
                request.setAttribute("listaProdutos", produtos);
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao carregar clientes: " + mensagemBanco(e));
        }

        request.setAttribute("itens", itensDaSessao(request));
        request.getRequestDispatcher("form-entrega.jsp").forward(request, response);
    }

    private void adicionarProduto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String codigo = texto(request, "codigo");
        Integer quantidade = inteiro(request, "quantidade");
        List<String> errosProduto = new ArrayList<>();
        if (codigo.isEmpty()) {
            errosProduto.add("codigo do produto inválido");
        }
        if (quantidade == null || quantidade <= 0) {
            errosProduto.add("Quantidade inválida");
        }
        if (!errosProduto.isEmpty()) {
            request.setAttribute("erros", errosProduto);
            mostrarFormulario(request, response);
            return;
        }

        try (Connection connection = new ConnectionFactory().recuperarConexao()) {
            Produto produto = new ProdutoDAO(connection).buscarPorCodigo(codigo);
            if (produto == null) {
                request.setAttribute("erro", "Produto não encontrado. Cadastre o produto antes.");
                mostrarFormulario(request, response);
                return;
            }

            List<ItemEntrega> itens = itensDaSessao(request);
            for (ItemEntrega itemNaLista : itens) {
                if (itemNaLista.getProduto().getCodigo().equals(codigo)) {
                    request.setAttribute("erro", "Esse produto já está na entrega");
                    mostrarFormulario(request, response);
                    return;
                }
            }

            itens.add(new ItemEntrega(produto, quantidade, produto.getValor()));
            response.sendRedirect("lancar-entrega?continuar=s");
        } catch (SQLException | RuntimeException e) {
            request.setAttribute("erro", "Erro ao buscar produto: " + mensagemBanco(e));
            mostrarFormulario(request, response);
        }
    }

    private void removerProduto(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer idProduto = inteiro(request, "idProduto");
        List<ItemEntrega> itens = itensDaSessao(request);
        List<ItemEntrega> novaLista = new ArrayList<>();

        for (ItemEntrega item : itens) {
            if (idProduto == null || item.getProduto().getId() != idProduto) {
                novaLista.add(item);
            }
        }

        request.getSession().setAttribute("itensEntrega", novaLista);
        response.sendRedirect("lancar-entrega?continuar=s");
    }

    private String textoSessao(HttpServletRequest request, String nome) {
        Object valor = request.getSession().getAttribute(nome);
        if (valor == null) {
            return "";
        }
        return valor.toString();
    }

    @SuppressWarnings("unchecked")
    private List<ItemEntrega> itensDaSessao(HttpServletRequest request) {
        List<ItemEntrega> itens = (List<ItemEntrega>) request.getSession().getAttribute("itensEntrega");
        if (itens == null) {
            itens = new ArrayList<>();
            request.getSession().setAttribute("itensEntrega", itens);
        }
        return itens;
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

    private String mensagemBanco(Exception e) {
        Throwable causa = e.getCause();
        String texto = e.getMessage();
        if (causa != null) {
            texto = causa.getMessage();
        }
        return texto != null ? texto : e.getMessage();
    }
}
