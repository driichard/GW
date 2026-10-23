<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Produtos - Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css?v=2">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <div class="linha-titulo">
        <h1>Produtos</h1>
        <a class="botao" href="lancar-produto">Novo produto</a>
    </div>

    <c:if test="${not empty erro}">
        <p class="aviso">${erro}</p>
    </c:if>

    <table class="tabela">
        <thead>
            <tr>
                <th>Id</th>
                <th>Nome</th>
                <th>Tamanho</th>
                <th>Peso</th>
                <th>Valor unitário</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty listaProdutos}">
                <tr>
                    <td colspan="6">Nenhum produto cadastrado</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="produto" items="${listaProdutos}">
                    <tr>
                        <td>${produto.id}</td>
                        <td>${produto.nomeDoProduto}</td>
                        <td>${produto.tamanho}</td>
                        <td>${produto.peso} kg</td>
                        <td>${produto.valor} R$</td>
                        <td>
                            <div class="acoes">
                                <a class="botao-claro" href="lancar-produto?acao=editar&id=${produto.id}">Editar</a>
                                <form method="post" action="lista-produtos">
                                    <input type="hidden" name="acao" value="excluir">
                                    <input type="hidden" name="id" value="${produto.id}">
                                    <button class="botao-excluir" type="submit">Excluir</button>
                                </form>
                            </div>
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
