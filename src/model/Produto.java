package model;

public class Produto {
    private int id;
    private String tipoDeProduto;
    private double valor;
    private int quantidade;

    public Produto(int id, String tipoDeProduto, double valor, int quantidade) {
        this.id = id;
        this.tipoDeProduto = tipoDeProduto;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public String getTipoDeProduto() {
        return tipoDeProduto;
    }

    public void setTipoDeProduto(String tipoDeProduto) {
        this.tipoDeProduto = tipoDeProduto;
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
