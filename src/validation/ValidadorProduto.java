package validation;

import model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ValidadorProduto implements Validador <Produto> {

    @Override
    public List<String> validar (Produto produto) {
        List <String> errosProduto = new ArrayList<>();

        if (String.valueOf(produto.getId()) == null || !String.valueOf(produto.getId()).matches("\\d+")) {
            errosProduto.add("ID inválido");
        }

        if (String.valueOf(produto.getQuantidade()) == null || !String.valueOf(produto.getQuantidade()).matches("\\d+")) {
            errosProduto.add("Quantidade inválido");
        }

        if (produto.getValor() <= -1) {
            errosProduto.add("Valor inválido");
        }


        if (produto.getTipoDeProduto() == null || produto.getTipoDeProduto().matches("\\d+")) {
            errosProduto.add("Nome do Produto inválido");
        }

        return errosProduto;
    }
}
