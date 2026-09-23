package validation;

import model.Clientes;
import model.Produto;

public class ClienteValidacao implements Validacao {

    @Override
    public boolean validarCliente (Clientes cliente) {
        if (cliente.getCpf() == null
                || cliente.getCpf().isBlank()
                || cliente.getCpf().length() != 11 ) {
            System.out.println("Invalido!!! Digite novamente");
            return true;
        }
        return false;
    }
}
