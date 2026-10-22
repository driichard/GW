package validation;

import model.Entrega;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEntrega implements Validador<Entrega> {

    @Override
    public List<String> validar(Entrega entrega) {
        List<String> errosEntrega = new ArrayList<>();

        if (entrega.getOrigem() == null) {
            errosEntrega.add("Endereço de origem inválido");
        }

        if (entrega.getDestino() == null) {
            errosEntrega.add("Endereço de destino inválido");
        }

        return errosEntrega;
    }
}
