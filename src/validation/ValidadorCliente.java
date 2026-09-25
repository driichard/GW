package validation;

import model.Clientes;
import model.Endereco;

import java.util.ArrayList;
import java.util.List;

    public class ValidadorCliente implements Validador<Clientes> {

        @Override
        public List<String> validar(Clientes cliente) {

            List<String> erros = new ArrayList<>();

            // if (cliente.get() <= 0) {
            //  erros.add("ID inválido");
            //  }

            if (cliente.getNome() == null || cliente.getNome().isBlank()) {
                erros.add("Nome inválido");
            }

            if (cliente.getCpf() == null || !cliente.getCpf().matches("\\d{11}") || cliente.getCpf().matches(".*[a-zA-Z].*")) {
                erros.add("CPF inválido");
            }

            if (cliente.getTelefone() == null || !cliente.getTelefone().matches("\\d{11}") || cliente.getTelefone().matches(".*[a-zA-Z].*")) {
                erros.add("Telefone inválido");
            }

            return erros;
        }

    }

