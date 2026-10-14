package validation;

import model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ValidadorProduto implements Validador<Produto> {

    @Override
    public List<String> validar(Produto produto) {
        List<String> errosProduto = new ArrayList<>();

        if (produto.getCodigo() <= 0) {
            errosProduto.add("Código inválido");
        }

        if (produto.getValor() <= 0) {
            errosProduto.add("Valor declarado inválido");
        }

        if (produto.getNomeDoProduto() == null
                || produto.getNomeDoProduto().isBlank()
                || produto.getNomeDoProduto().matches("\\d+")) {
            errosProduto.add("Nome do produto inválido");
        }

        if (produto.getPeso() <= 0) {
            errosProduto.add("Peso inválido");
        }

        if (produto.getComprimento() <= 0 || produto.getAltura() <= 0 || produto.getLargura() <= 0) {
            errosProduto.add("Medidas inválidas");
        }

        return errosProduto;
    }
}
