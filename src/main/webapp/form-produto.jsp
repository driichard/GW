<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${editar ? 'Editar produto' : 'Novo produto'} - Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <div class="linha-titulo">
        <h1>${editar ? 'Editar produto' : 'Novo produto'}</h1>
        <a class="botao-claro" href="lista-produtos">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <p class="aviso">${erro}</p>
    </c:if>

    <c:forEach var="mensagem" items="${erros}">
        <p class="aviso">${mensagem}</p>
    </c:forEach>

    <form class="formulario" method="post" action="lancar-produto">
        <input type="hidden" name="acao" value="${editar ? 'atualizarProduto' : 'cadastrarProduto'}">
        <c:if test="${editar}">
            <input maxlength="5"type="hidden" name="id" value="${produto.id}">
        </c:if>

        <div class="campos">
            <div class="campo">
                <label>Nome do produto</label>
                <input maxlength="50" type="text" name="nome" value="${produto.nomeDoProduto}">
            </div>
            <div class="compo">
                <label> Código do produto</label>
                <input maxlength="5" type="text" name="codigo" value="${produto.codigo}">
            </div>
            <div class="campo">
                <label>Peso (kg)</label>
                <input maxlength="10" type="text" name="peso" value="${produto.peso}">
            </div>
            <div class="campo">
                <label>Comprimento (cm)</label>
                <input maxlength="10" type="text" name="comprimento" value="${produto.comprimento}">
            </div>
            <div class="campo">
                <label>Altura (cm)</label>
                <input maxlength="10" type="text" name="altura" value="${produto.altura}">
            </div>
            <div class="campo">
                <label>Largura (cm)</label>
                <input maxlength="10" type="text" name="largura" value="${produto.largura}">
            </div>
            <div class="campo">
                <label>Valor unitário</label>
                <input maxlength="10" type="text" name="valor" value="${produto.valor}">
            </div>
        </div>
        <div class="linha-acoes">
            <button class="botao" type="submit">${editar ? 'Atualizar produto' : 'Cadastrar produto'}</button>
        </div>
    </form>
</main>

</body>
</html>
