<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Rota Certa</title>
    <link rel="stylesheet" href="css/estilo.css">
</head>
<body>

<jsp:include page="cabecalho.jsp"/>

<main class="pagina">
    <h1>Início</h1>
    <p class="subtitulo">Escolha o que deseja fazer no sistema de entregas da Rota Certa.</p>

    <div class="grade">
        <a class="cartao" href="lista-clientes">
            <h2>Cliente</h2>
            <p>Cadastrar, editar e excluir remetentes e destinatários.</p>
        </a>
        <a class="cartao" href="lista-produtos">
            <h2>Produto</h2>
            <p>Cadastrar, editar e excluir produtos com estoque.</p>
        </a>
        <a class="cartao" href="lista-entregas">
            <h2>Entrega</h2>
            <p>Criar entregas e acompanhar o status de cada uma.</p>
        </a>
        <a class="cartao" href="simular">
            <h2>Simular frete</h2>
            <p>Calcular uma estimativa de frete sem gravar nada.</p>
        </a>
    </div>
</main>

</body>
</html>
