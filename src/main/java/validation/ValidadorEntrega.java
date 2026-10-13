package validation;

import model.Entrega;
import model.ItemEntrega;


import java.util.ArrayList;
import java.util.List;

public class ValidadorEntrega implements validation.Validador<Entrega> {

    @Override
    public List<String> validar(Entrega entrega) {
        List<String> errosEntrega = new ArrayList<>();

        if (entrega.getCodigo() <= 0) {
            errosEntrega.add("Identificador inválido");
        }

        if (entrega.getValorEntrega() < 0) {
            errosEntrega.add("Valor do frete inválido");
        }

        if (entrega.getRemetente() == null) {
            errosEntrega.add("Remetente inválido");
        }

        if (entrega.getDestinatario() == null) {
            errosEntrega.add("Destinatário inválido");
        }

        if (entrega.getItens() == null || entrega.getItens().isEmpty()) {
            errosEntrega.add("Entrega sem produtos");
        } else {
            for (ItemEntrega item : entrega.getItens()) {
                if (item.getQuantidade() <= 0) {
                    errosEntrega.add("Quantidade do item inválida");
                }

                if (item.getProduto() == null) {
                    errosEntrega.add("Produto do item inválido");
                }
            }
        }

        if (entrega.getStatus() == null) {
            errosEntrega.add("Status inválido");
        }

        return errosEntrega;
    }
}
