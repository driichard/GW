package main.java.validation;

import main.java.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ValidadorProduto implements validation.Validador<Produto> {

    @Override
    public List<String> validar(Produto produto) {
        List<String> errosProduto = new ArrayList<>();

        if (produto.getCodigo() <= -1) {
            errosProduto.add("Identificador inválido");
        }

        if (produto.getValor() <= 0) {
            errosProduto.add("Valor declarado inválido");
        }

        if (produto.getNomeDoProduto() == null
                || produto.getNomeDoProduto().isBlank()
                || produto.getNomeDoProduto().matches("\\d+")) {
            errosProduto.add("Nome do volume inválido");
        }

        return errosProduto;
    }
}
