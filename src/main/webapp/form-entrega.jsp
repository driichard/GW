<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>${editar ? 'Editar entrega' : 'Nova entrega'} - Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <div class="linha-titulo">
        <h1>${editar ? 'Editar entrega' : 'Nova entrega'}</h1>
        <a class="botao-claro" href="lista-entregas">Voltar</a>
    </div>

    <c:if test="${not empty erro}">
        <p class="aviso">${erro}</p>
    </c:if>

    <c:forEach var="mensagem" items="${erros}">
        <p class="aviso">${mensagem}</p>
    </c:forEach>

    <form class="formulario" method="post" action="lancar-entrega">
        <div class="campos">
            <div class="campo">
                <label>CPF ou CNPJ do remetente</label>
                <input maxlength="14" type="text" name="documentoRemetente" value="${documentoRemetente}">
            </div>
            <div class="campo">
                <label>CPF ou CNPJ do destinatário</label>
                <input maxlength="14" type="text" name="documentoDestinatario" value="${documentoDestinatario}">
            </div>
            <div class="campo">
                <label>Código do produto</label>
                <input maxlength="5" type="text" name="codigo">
            </div>
            <div class="campo">
                <label>Quantidade</label>
                <input maxlength="50" type="text" name="quantidade">
            </div>
        </div>

        <div class="linha-acoes">
            <button class="botao-claro" type="submit" name="acao" value="adicionar">Adicionar produto</button>
            <button class="botao" type="submit" name="acao" value="salvar">${editar ? 'Atualizar entrega' : 'Cadastrar entrega'}</button>
        </div>
    </form>

    <c:if test="${not empty itens}">
        <div class="formulario">
            <h2>Produtos da entrega</h2>
            <c:forEach var="item" items="${itens}">
                <div class="linha-caixa">
                    <p>
                        ${item.quantidade}x [${item.produto.codigo}]
                        ${item.produto.nomeDoProduto}
                        | Valor unitário: ${item.valorUnitario} R$
                    </p>
                    <form method="post" action="lancar-entrega">
                        <input type="hidden" name="acao" value="remover">
                        <input type="hidden" name="documentoRemetente" value="${documentoRemetente}">
                        <input type="hidden" name="documentoDestinatario" value="${documentoDestinatario}">
                        <input type="hidden" name="idProduto" value="${item.produto.codigo}">
                        <button class="botao-excluir" type="submit">Remover</button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </c:if>
</main>

</body>
</html>
