package validation;

import model.Endereco;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEndereco implements Validador<Endereco> {


    @Override
    public List<String> validar(Endereco endereco) {

        List<String> errosEndereco = new ArrayList<>();

        if (endereco.getCep() == null || endereco.getCep().isBlank() || !endereco.getCep().matches("//d+")) {
            errosEndereco.add("Cep invalido!");
        }

        if (endereco.getRua() == null || endereco.getRua().isBlank()){
            errosEndereco.add("Rua invalida!");
        }

        if (endereco.getBairro() == null || endereco.getBairro().isBlank()){
            errosEndereco.add("Bairro invalido");
        }

        if (endereco.getCidade() == null || endereco.getCidade().isBlank() || !endereco.getCep().matches("//d+")) {
            errosEndereco.add("Endereço invalido!");
        }

        if (endereco.getEstado() == null || endereco.getEstado().isBlank() || !endereco.getCep().matches("//d+")) {
            errosEndereco.add("Estado invalido!");
        }

        if (endereco.getNumero() == null || endereco.getNumero().isBlank() || !endereco.getCep().matches("//d+")) {
            errosEndereco.add("Numero nvalido!");
        }

        return errosEndereco;


    }
}
