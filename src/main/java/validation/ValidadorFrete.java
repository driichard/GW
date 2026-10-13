package validation;

import model.ItemEntrega;

import java.util.ArrayList;
import java.util.List;

public class ValidadorFrete implements validation.Validador<List<ItemEntrega>> {

    @Override
    public List<String> validar(List<ItemEntrega> itens) {
        List<String> erros = new ArrayList<>();

        if (itens == null || itens.isEmpty()) {
            erros.add("Frete sem produtos");
            return erros;
        }

        for (ItemEntrega item : itens) {
            if (item.getProduto() == null) {
                erros.add("Produto do frete inválido");
                continue;
            }

            if (item.getProduto().getVolume() <= 0) {
                erros.add("Volume inválido");
            }

            if (item.getProduto().getPeso() <= 0) {
                erros.add("Peso inválido");
            }

            if (item.getQuantidade() <= 0) {
                erros.add("Quantidade inválida");
            }
        }

        return erros;
    }

    public List<String> validar(double comprimento, double altura, double largura, double peso, int quantidade) {
        List<String> erros = new ArrayList<>();

        if (comprimento <= 0 || altura <= 0 || largura <= 0) {
            erros.add("Medidas inválidas");
        }

        if (peso <= 0) {
            erros.add("Peso inválido");
        }

        if (quantidade <= 0) {
            erros.add("Quantidade inválida");
        }

        return erros;
    }

    public List<String> validarValor(double frete) {
        List<String> erros = new ArrayList<>();

        if (frete < 0) {
            erros.add("Valor do frete inválido");
        }

        return erros;
    }
}
