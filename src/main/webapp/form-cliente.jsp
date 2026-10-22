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
        </c:if>

        <div class="campos">
            <div class="campo">
                <label>Nome</label>
                <input type="text" name="nome" value="${cliente.nome}">
            </div>
            <div class="campo">
                <label>CPF ou CNPJ (somente números)</label>
                <input type="text" name="documento" value="${cliente.documento}">
            </div>
            <div class="campo">
                <label>Telefone (11 números, com DDD)</label>
                <input type="text" name="telefone" value="${cliente.telefone}">
            </div>
        </div>

        <c:if test="${editar}">
            <h2>Endereços cadastrados</h2>
            <c:forEach var="endereco" items="${cliente.enderecos}" varStatus="st">
                <div class="bloco-endereco">
                    <input type="hidden" name="enderecoId" value="${endereco.id}">
                    <h3>Endereço ${st.count}</h3>
                    <div class="campos">
                        <div class="campo">
                            <label>CEP (8 números)</label>
                            <input type="text" name="cep_${endereco.id}" value="${endereco.cep}">
                        </div>
                        <div class="campo">
                            <label>Rua</label>
                            <input type="text" name="rua_${endereco.id}" value="${endereco.rua}">
                        </div>
                        <div class="campo">
                            <label>Número (somente números)</label>
                            <input type="text" name="numero_${endereco.id}" value="${endereco.numero}">
                        </div>
                        <div class="campo">
                            <label>Bairro</label>
                            <input type="text" name="bairro_${endereco.id}" value="${endereco.bairro}">
                        </div>
                        <div class="campo">
                            <label>Cidade</label>
                            <input type="text" name="cidade_${endereco.id}" value="${endereco.cidade}">
                        </div>
                        <div class="campo">
                            <label>Estado (2 letras, ex: SP)</label>
                            <input type="text" name="estado_${endereco.id}" value="${endereco.estado}">
                        </div>
                    </div>
                </div>
            </c:forEach>
            <div id="novo-endereco" class="bloco-endereco">
                <h2>Adicionar mais um endereço</h2>
                <p class="subtitulo">Preencha só se quiser cadastrar outro endereço neste cliente.</p>
                <div class="campos">
                    <div class="campo">
                        <label>CEP (8 números)</label>
                        <input type="text" name="cep" value="${novoEndereco.cep}">
                    </div>
                    <div class="campo">
                        <label>Rua</label>
                        <input type="text" name="rua" value="${novoEndereco.rua}">
                    </div>
                    <div class="campo">
                        <label>Número (somente números)</label>
                        <input type="text" name="numero" value="${novoEndereco.numero}">
                    </div>
                    <div class="campo">
                        <label>Bairro</label>
                        <input type="text" name="bairro" value="${novoEndereco.bairro}">
                    </div>
                    <div class="campo">
                        <label>Cidade</label>
                        <input type="text" name="cidade" value="${novoEndereco.cidade}">
                    </div>
                    <div class="campo">
                        <label>Estado (2 letras, ex: SP)</label>
                        <input type="text" name="estado" value="${novoEndereco.estado}">
                    </div>
                </div>
            </div>
        </c:if>

        <c:if test="${not editar}">
        <div class="campos">
            <div class="campo">
                <label>CEP (8 números)</label>
                <input type="text" name="cep" value="${cliente.endereco.cep}">
            </div>
            <div class="campo">
                <label>Rua</label>
                <input type="text" name="rua" value="${cliente.endereco.rua}">
            </div>
            <div class="campo">
                <label>Número (somente números)</label>
                <input type="text" name="numero" value="${cliente.endereco.numero}">
            </div>
            <div class="campo">
                <label>Bairro</label>
                <input type="text" name="bairro" value="${cliente.endereco.bairro}">
            </div>
            <div class="campo">
                <label>Cidade</label>
                <input type="text" name="cidade" value="${cliente.endereco.cidade}">
            </div>
            <div class="campo">
                <label>Estado (2 letras, ex: SP)</label>
                <input type="text" name="estado" value="${cliente.endereco.estado}">
            </div>
        </div>
        </c:if>

        <div class="linha-acoes">
            <button class="botao" type="submit">${editar ? 'Salvar alteração' : 'Cadastrar cliente'}</button>
        </div>
    </form>
</main>
<c:if test="${param.foco eq 'novo-endereco'}">
<script>
    var bloco = document.getElementById('novo-endereco');
    if (bloco) {
        bloco.scrollIntoView();
    }
</script>
</c:if>

</body>
</html>
