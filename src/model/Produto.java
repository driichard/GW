package model;

public class Produto {
    private int codigo;
    private String nomeDoProduto;
    private double valor;

    public Produto(int codigo, String nomeDoProduto, double valor) {
        this.codigo = codigo;
        this.nomeDoProduto = nomeDoProduto;
        this.valor = valor;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNomeDoProduto() {
        return nomeDoProduto;
    }

    public double getValor() {
        return valor;
    }
}
