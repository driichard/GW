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

    <table class="tabela">
        <thead>
            <tr>
                <th>Nome</th>
                <th>Documento</th>
                <th>Telefone</th>
                <th>Endereço</th>
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
                        <td class="doc"> ${cliente.documento}</td>
                        <td class="tel">${cliente.telefone}</td>
                        <td>${cliente.endereco.texto}</td>
                        <td>
                            <div class="acoes">
                                <a class="botao-claro" href="lancar-cliente?acao=editar&documento=${cliente.documento}">Editar</a>
                                <form method="post" action="lista-clientes">
                                    <input type="hidden" name="acao" value="excluir">
                                    <input type="hidden" name="documento" value="${cliente.documento}">
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

<script>
function formatarDocumento(n) {
    n = (n || "").replace(/\D/g, "");
    if (n.length <= 11) {
        return n.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2})/, function (_, a, b, c, d) {
            return a + "." + b + "." + c + (d ? "-" + d : "");
        });
    }
    return n.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{0,2})/, function (_, a, b, c, d, e) {
        return a + "." + b + "." + c + "/" + d + (e ? "-" + e : "");
    });
}

function formatarTelefone(n) {
    n = (n || "").replace(/\D/g, "").slice(0, 11);
    if (n.length > 6) {
        return "(" + n.slice(0, 2) + ") " + n.slice(2, 7) + "-" + n.slice(7);
    }
    if (n.length > 2) {
        return "(" + n.slice(0, 2) + ") " + n.slice(2);
    }
    return n;
}

document.querySelectorAll("td.doc").forEach(function (td) {
    td.textContent = formatarDocumento(td.textContent.trim());
});
document.querySelectorAll("td.tel").forEach(function (td) {
    td.textContent = formatarTelefone(td.textContent.trim());
});
</script>

</body>
</html>
