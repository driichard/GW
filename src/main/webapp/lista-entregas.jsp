<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Entregas - Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css?v=2">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <div class="linha-titulo">
        <h1>Entregas</h1>
        <a class="botao" href="lancar-entrega">Nova entrega</a>
    </div>

    <c:if test="${not empty erro}">
        <p class="aviso">${erro}</p>
    </c:if>

    <table class="tabela">
        <thead>
            <tr>
                <th>Id</th>
                <th>Remetente</th>
                <th>Destinatário</th>
                <th>Origem</th>
                <th>Destino</th>
                <th>Status</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty listaEntregas}">
                <tr>
                    <td colspan="7">Nenhuma entrega cadastrada</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="entrega" items="${listaEntregas}">
                    <c:set var="classeStatus" value="status-pendente"/>
                    <c:if test="${entrega.status eq 'EM_ANDAMENTO'}">
                        <c:set var="classeStatus" value="status-andamento"/>
                    </c:if>
                    <c:if test="${entrega.status eq 'ENTREGUE'}">
                        <c:set var="classeStatus" value="status-entregue"/>
                    </c:if>
                    <c:if test="${entrega.status eq 'CANCELADA'}">
                        <c:set var="classeStatus" value="status-cancelada"/>
                    </c:if>
                    <tr>
                        <td>${entrega.id}</td>
                        <td>${entrega.remetente.nome}</td>
                        <td>${entrega.destinatario.nome}</td>
                        <td>${entrega.origem.cidade}/${entrega.origem.estado}</td>
                        <td>${entrega.destino.cidade}/${entrega.destino.estado}</td>
                        <td><span class="status ${classeStatus}">${entrega.status}</span></td>
                        <td>
                            <div class="acoes">
                                <c:if test="${entrega.status eq 'PENDENTE'}">
                                    <a class="botao-claro" href="lancar-entrega?acao=editar&id=${entrega.id}">Editar</a>
                                    <a class="botao-claro" href="lista-entregas?acao=iniciar&id=${entrega.id}">Iniciar</a>
                                </c:if>
                                <c:if test="${entrega.status eq 'EM_ANDAMENTO'}">
                                    <a class="botao-verde" href="lista-entregas?acao=concluir&id=${entrega.id}">Concluir</a>
                                </c:if>
                                <c:if test="${entrega.status ne 'ENTREGUE' and entrega.status ne 'CANCELADA'}">
                                    <a class="botao-claro" href="lista-entregas?acao=cancelar&id=${entrega.id}">Cancelar</a>
                                </c:if>
                                <c:if test="${entrega.status eq 'ENTREGUE' or entrega.status eq 'CANCELADA'}">
                                    <a class="botao-excluir" href="lista-entregas?acao=excluir&id=${entrega.id}">Excluir</a>
                                </c:if>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="detalhe-caixa" colspan="7">
                            <c:forEach var="item" items="${entrega.itens}">
                                <p>
                                    ${item.quantidade}x [${item.produto.id}]
                                    ${item.produto.nomeDoProduto}
                                    | Valor unitário: ${item.valorUnitario} R$
                                </p>
                            </c:forEach>
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
