package validation;

import model.Entrega;
import model.ItemEntrega;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEntrega implements Validador<Entrega> {

    @Override
    public List<String> validar(Entrega entrega) {
        List<String> errosEntrega = new ArrayList<>();

        if (entrega.getCodigo() <= 0) {
            errosEntrega.add("Identificador inválido");
        }

        if (entrega.getValorEntrega() < 0) {
            errosEntrega.add("Valor do frete inválido");
        }

        if (entrega.getCliente() == null) {
            errosEntrega.add("Informações clientes inválido");
        }

        if (entrega.getItens() == null || entrega.getItens().isEmpty()) {
            errosEntrega.add("Entrega sem volumes");
        } else {
            for (ItemEntrega item : entrega.getItens()) {
                if (item.getQuantidade() <= 0) {
                    errosEntrega.add("Quantidade do item inválida");
                }

                if (item.getProduto() == null) {
                    errosEntrega.add("Volume do item inválido");
                }
            }
        }

        if (entrega.getStatus() == null) {
            errosEntrega.add("Status inválido");
        }

        return errosEntrega;
    }
}
