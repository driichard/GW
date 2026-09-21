package model;

public class Clientes {
    private String cpf;
    private String telefone;
    private String nome;
    private Endereco endereco;

    public Clientes(String telefone, String cpf, String nome, Endereco endereco) {
        this.telefone = telefone;
        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
    }

    public String getCpf() {
        return cpf;
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

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
