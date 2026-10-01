package model;

public class Produto {
    private int id;
    private String nomeDoProduto;
    private double valor;
    private int quantidade;

    public Produto(int id, String nomeDoProduto, double valor, int quantidade) {
        this.id = id;
        this.nomeDoProduto = nomeDoProduto;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public String getNomeDoProduto() {
        return nomeDoProduto;
    }

    public void setNomeDoProduto(String nomeDoProduto) {
        this.nomeDoProduto = nomeDoProduto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

}

