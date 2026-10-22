package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String documento;
    private String telefone;
    private String nome;
    private List<Endereco> enderecos;

    public Cliente(String telefone, String documento, String nome, List<Endereco> enderecos) {
        this.telefone = telefone;
        this.documento = documento;
        this.nome = nome;
        this.enderecos = enderecos;
        if (this.enderecos == null) {
            this.enderecos = new ArrayList<>();
        }
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getNome() {
        return nome;
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public Endereco getEndereco() {
        if (enderecos.isEmpty()) {
            return null;
        }
        return enderecos.get(0);
    }

    public Endereco buscarEndereco(int id) {
        for (Endereco endereco : enderecos) {
            if (endereco.getId() == id) {
                return endereco;
            }
        }
        return null;
    }
}
