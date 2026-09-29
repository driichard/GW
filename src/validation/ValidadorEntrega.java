package validation;

import model.Endereco;
import model.Entrega;

import javax.management.StringValueExp;
import java.util.ArrayList;
import java.util.List;

public class ValidadorEntrega implements Validador<Entrega> {

    @Override
    public List<String> validar(Entrega entrega){
        List <String> errosEntrega = new ArrayList<>();

        if (String.valueOf(entrega.getId()) == null || !String.valueOf(entrega.getId()).matches("\\d+")) {
            errosEntrega.add("ID inválido");
        }

        if (entrega.getValorEntrega() <= -1) {
            errosEntrega.add("Valor inválido");
        }

        if (entrega.getCliente() == null ) {
            errosEntrega.add("Informações clientes inválido");
        }

        if (entrega.getStatus() == null || entrega.getStatus().isBlank() || entrega.getStatus().matches("\\d+")){
            errosEntrega.add("Valor inválido");
        }


        return errosEntrega;
    }
}
