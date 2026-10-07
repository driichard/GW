package main.java.validation;
import main.java.model.Endereco;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEndereco implements validation.Validador<Endereco> {


    @Override
    public List<String> validar(Endereco endereco) {

        List<String> errosEndereco = new ArrayList<>();

        if (endereco.getCep() == null
                || endereco.getCep().isBlank()
                || !endereco.getCep().matches("\\d+")) {
            errosEndereco.add("Cep inválido!");
        }

        if (endereco.getRua() == null || endereco.getRua().isBlank()){
            errosEndereco.add("Rua inválido!");
        }

        if (endereco.getBairro() == null || endereco.getBairro().isBlank()){
            errosEndereco.add("Bairro inválido");
        }

        if (endereco.getCidade() == null
                || endereco.getCidade().isBlank()
                || endereco.getCidade().matches("\\d+")) {
            errosEndereco.add("Cidade inválido!");
        }

        if (endereco.getEstado() == null
                || endereco.getEstado().isBlank()
                || endereco.getEstado().matches("\\d+")) {
            errosEndereco.add("Estado inválido!");
        }

        if (endereco.getNumero() == null
                || endereco.getNumero().isBlank()
                || !endereco.getNumero().matches("\\d+")) {
            errosEndereco.add("Numero inválido!");
        }

        return errosEndereco;

    }
}
