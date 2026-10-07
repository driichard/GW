package main.java.validation;

import main.java.model.Clientes;

import java.util.ArrayList;
import java.util.List;

public class ValidadorCliente implements validation.Validador<Clientes> {

    @Override
    public List<String> validar(Clientes cliente) {
        List<String> erros = new ArrayList<>();

        if (cliente.getNome() == null || cliente.getNome().isBlank()) {
            erros.add("Nome inválido");
        }

        if (cliente.getDocumento() == null
                || cliente.getDocumento().isBlank()
                || !cliente.getDocumento().matches("\\d{11}|\\d{14}")) {
            erros.add("CPF OU CNPJ inválido");
        }

        if (cliente.getTelefone() == null
                || cliente.getTelefone().isBlank()
                || !cliente.getTelefone().matches("\\d{11}")) {
            erros.add("Telefone inválido");
        }

        return erros;
    }
}
