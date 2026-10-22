import model.Cliente;
import validation.ValidadorCliente;
import java.util.ArrayList;
import java.util.List;

public class TestaDocumento {
    public static void main(String[] args) {
        ValidadorCliente v = new ValidadorCliente();
        String[] docs = {
            "52998224725", "39053344705", "11111111111", "12345678901", "71062565444",
            "11222333000181", "11444777000161", "12345678000199", "00000000000000", "123"
        };
        for (String doc : docs) {
            Cliente c = new Cliente("11999999999", doc, "Maria", new ArrayList<>());
            List<String> erros = v.validar(c);
            System.out.println(doc + " -> " + (erros.isEmpty() ? "ok" : erros));
        }
    }
}
