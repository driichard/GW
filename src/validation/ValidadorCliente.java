package validation;

import model.Clientes;

import java.util.ArrayList;
import java.util.List;

    public class ValidadorCliente implements Validador<Clientes> {

        @Override
        public List<String> validar(Clientes cliente) {

            List<String> erros = new ArrayList<>();

            if (cliente.getNome() == null || cliente.getNome().isBlank()) {
                erros.add("Nome inválido");
            }

            if (cliente.getDocumento() == null || !cliente.getDocumento().matches("\\d{14}") || cliente.getDocumento().isBlank()) {
                erros.add("CPF OU CNPJ inválido");
            }

            if (cliente.getTelefone() == null || !cliente.getTelefone().matches("\\d{11}") || cliente.getTelefone().isBlank()) {
                erros.add("Telefone inválido");
            }

            return erros;
        }

    }

