package validation;

import java.util.ArrayList;
import java.util.List;

public class ValidadorFrete {

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
}
