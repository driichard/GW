<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Clientes - Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <div class="linha-titulo">
        <h1>Clientes</h1>
        <a class="botao" href="lancar-cliente">Novo cliente</a>
    </div>

    <c:if test="${not empty erro}">
        <p class="aviso">${erro}</p>
    </c:if>

    <form class="busca" method="post" action="lista-clientes">
        <input type="text" name="busca" value="${busca}" placeholder="Nome ou CPF/CNPJ">
        <button class="botao-claro" type="submit">Buscar</button>
        <c:if test="${not empty busca}">
            <a class="botao-claro" href="lista-clientes">Limpar</a>
        </c:if>
    </form>

    <table class="tabela">
        <thead>
            <tr>
                <th>Nome</th>
                <th>Documento</th>
                <th>Telefone</th>
                <th>Endereços</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty listaClientes}">
                <tr>
                    <td colspan="5">Nenhum cliente cadastrado</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="cliente" items="${listaClientes}">
                    <tr>
                        <td>${cliente.nome}</td>
                        <td>${cliente.documento}</td>
                        <td>${cliente.telefone}</td>
                        <td>
                            <c:forEach var="endereco" items="${cliente.enderecos}">
                                <p>${endereco.texto}</p>
                            </c:forEach>
                        </td>
                        <td>
                            <form method="post" action="lancar-cliente">
                                <input type="hidden" name="acao" value="editar">
                                <input type="hidden" name="documento" value="${cliente.documento}">
                                <button class="botao-claro" type="submit">Editar</button>
                            </form>
                            <form method="post" action="lancar-cliente">
                                <input type="hidden" name="acao" value="editar">
                                <input type="hidden" name="foco" value="novo-endereco">
                                <input type="hidden" name="documento" value="${cliente.documento}">
                                <button class="botao-claro" type="submit">Adicionar endereço</button>
                            </form>
                            <form method="post" action="lista-clientes">
                                <input type="hidden" name="acao" value="excluir">
                                <input type="hidden" name="documento" value="${cliente.documento}">
                                <button class="botao-excluir" type="submit">Excluir</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</main>

</body>
</html>
