<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="topo">
    <a class="marca" href="index.jsp">
        <span class="marca-bola"></span>
        Rota Certa
    </a>
    <nav>
        <a class="${menuAtivo == 'clientes' ? 'ativo' : ''}" href="lista-clientes">Cliente</a>
        <a class="${menuAtivo == 'produtos' ? 'ativo' : ''}" href="lista-produtos">Produto</a>
        <a class="${menuAtivo == 'entregas' ? 'ativo' : ''}" href="lista-entregas">Entrega</a>
        <a class="${menuAtivo == 'simular' ? 'ativo' : ''}" href="simular">Simular frete</a>
    </nav>
</header>
