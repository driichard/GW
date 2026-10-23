package validation;

import model.Entrega;
import model.ItemEntrega;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEntrega implements Validador<Entrega> {

    @Override
    public List<String> validar(Entrega entrega) {
        List<String> errosEntrega = new ArrayList<>();

        if (entrega.getRemetente() == null) {
            errosEntrega.add("Remetente inválido");
        }

        if (entrega.getDestinatario() == null) {
            errosEntrega.add("Destinatário inválido");
        }

        if (entrega.getRemetente() != null
                && entrega.getDestinatario() != null
                && entrega.getRemetente().getDocumento().equals(entrega.getDestinatario().getDocumento())) {
            errosEntrega.add("Remetente e destinatário precisam ser diferentes");
        }

        if (entrega.getItens() == null || entrega.getItens().isEmpty()) {
            errosEntrega.add("Entrega sem produtos");
        } else {
            for (ItemEntrega item : entrega.getItens()) {
                if (item.getQuantidade() <= 0) {
                    errosEntrega.add("Quantidade do item inválida");
                }
            }
        }

        if (entrega.getOrigem() == null) {
            errosEntrega.add("Endereço de origem inválido");
        }

        if (entrega.getDestino() == null) {
            errosEntrega.add("Endereço de destino inválido");
        }

        return errosEntrega;
    }
}
