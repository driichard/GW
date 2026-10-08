package model;

public class Clientes {
    private String documento;
    private String telefone;
    private String nome;
    private Endereco endereco;

    public Clientes(String telefone, String documento, String nome, Endereco endereco) {
        this.telefone = telefone;
        this.documento = documento;
        this.nome = nome;
        this.endereco = endereco;
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

    public Endereco getEndereco() {
        return endereco;
    }




}
