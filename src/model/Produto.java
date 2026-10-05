package model;

public class Produto {
    private int identificador;
    private String nomeDoProduto;
    private double valor;

    public Produto(int identificador, String nomeDoProduto, double valor) {
        this.identificador = identificador;
        this.nomeDoProduto = nomeDoProduto;
        this.valor = valor;
    }

    public int getIdentificador() {
        return identificador;
    }

    public String getNomeDoProduto() {
        return nomeDoProduto;
    }

    public double getValor() {
        return valor;
    }
}
