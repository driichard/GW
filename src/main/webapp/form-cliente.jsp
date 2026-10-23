<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${editar ? 'Editar cliente' : 'Novo cliente'} - Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <div class="linha-titulo">
        <h1>${editar ? 'Editar cliente' : 'Novo cliente'}</h1>
        <a class="botao-claro" href="lista-clientes">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <p class="aviso">${erro}</p>
    </c:if>

    <c:forEach var="mensagem" items="${erros}">
        <p class="aviso">${mensagem}</p>
    </c:forEach>

    <form class="formulario" method="post" action="lancar-cliente">
        <input type="hidden" name="acao" value="${editar ? 'salvarAlteracao' : 'cadastrarCliente'}">
        <c:if test="${editar}">
            <input type="hidden" name="documentoAtual" value="${empty documentoAtual ? cliente.documento : documentoAtual}">
            <input type="hidden" name="enderecoId" value="${cliente.endereco.id}">
        </c:if>

        <div class="campos">
            <div class="campo">
                <label>Nome</label>
                <input maxlength="50" type="text" name="nome" value="${cliente.nome}">
            </div>
            <div class="campo">
                <label>CPF ou CNPJ (somente números)</label>
                <input maxlength="14" type="text" name="documento" value="${cliente.documento}">
            </div>
            <div class="campo">
                <label>Telefone (11 números, com DDD)</label>
                <input maxlength="11" type="text" name="telefone" value="${cliente.telefone}">
            </div>
            <div class="campo">
                <label>CEP (8 números)</label>
                <input maxlength="8" type="text" name="cep" value="${cliente.endereco.cep}">
            </div>
            <div class="campo">
                <label>Rua</label>
                <input maxlength="50" type="text" name="rua" value="${cliente.endereco.rua}">
            </div>
            <div class="campo">
                <label>Número (somente números)</label>
                <input maxlength="8" type="text" name="numero" value="${cliente.endereco.numero}">
            </div>
            <div class="campo">
                <label>Bairro</label>
                <input maxlength="30" type="text" name="bairro" value="${cliente.endereco.bairro}">
            </div>
            <div class="campo">
                <label>Cidade</label>
                <input maxlength="50" type="text" name="cidade" value="${cliente.endereco.cidade}">
            </div>
            <div class="campo">
                <label>Estado (2 letras, ex: SP)</label>
                <input maxlength="2" type="text" name="estado" value="${cliente.endereco.estado}">
            </div>
        </div>

        <div class="linha-acoes">
            <button class="botao" type="submit">${editar ? 'Salvar alteração' : 'Cadastrar cliente'}</button>
        </div>
    </form>
</main>

</body>
</html>
