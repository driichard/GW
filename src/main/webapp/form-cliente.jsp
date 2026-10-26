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
                <input maxlength="18" type="text" name="documento" id="documento" value="${cliente.documento}" placeholder="000.000.000-00" >
            </div>
            <div class="campo">
                <label>Telefone (11 números, com DDD)</label>
                <input maxlength="15" type="text" name="telefone" id="telefone" value="${cliente.telefone}" placeholder="(00) 00000-0000">
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


<script>
document.addEventListener("DOMContentLoaded", function () {
    var telefone = document.getElementById("telefone");
    var documento = document.getElementById("documento");
    var form = document.querySelector("form.formulario");
    var telAnterior = "";
    var docAnterior = "";

    telefone.addEventListener("input", function (e) {
        var n = this.value.replace(/\D/g, "").slice(0, 11);

        if (e.inputType && e.inputType.indexOf("delete") === 0 && n.length === telAnterior.length && n.length > 0) {
            n = n.slice(0, -1);
        }
        telAnterior = n;

        if (n.length > 6) {
            this.value = "(" + n.slice(0, 2) + ") " + n.slice(2, 7) + "-" + n.slice(7);
        } else if (n.length > 2) {
            this.value = "(" + n.slice(0, 2) + ") " + n.slice(2);
        } else if (n.length > 0) {
            this.value = "(" + n;
        } else {
            this.value = "";
        }
    });

    documento.addEventListener("input", function (e) {
        var n = this.value.replace(/\D/g, "").slice(0, 14);

        if (e.inputType && e.inputType.indexOf("delete") === 0 && n.length === docAnterior.length && n.length > 0) {
            n = n.slice(0, -1);
        }
        docAnterior = n;

        if (n.length <= 11) {
            if (n.length > 9) {
                this.value = n.slice(0, 3) + "." + n.slice(3, 6) + "." + n.slice(6, 9) + "-" + n.slice(9);
            } else if (n.length > 6) {
                this.value = n.slice(0, 3) + "." + n.slice(3, 6) + "." + n.slice(6);
            } else if (n.length > 3) {
                this.value = n.slice(0, 3) + "." + n.slice(3);
            } else {
                this.value = n;
            }
        } else {
            this.value = n.slice(0, 2) + "." + n.slice(2, 5) + "." + n.slice(5, 8)
                + "/" + n.slice(8, 12) + "-" + n.slice(12);
        }
    });

    form.addEventListener("submit", function () {
        telefone.value = telefone.value.replace(/\D/g, "");
        documento.value = documento.value.replace(/\D/g, "");
    });
});
</script>


</body>
</html>
