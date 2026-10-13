package model;

public class Produto {
    private int codigo;
    private String nomeDoProduto;
    private double valor;
    private double peso;
    private double comprimento;
    private double altura;
    private double largura;

    public Produto(int codigo, String nomeDoProduto, double valor, double peso, double comprimento, double altura, double largura) {
        this.codigo = codigo;
        this.nomeDoProduto = nomeDoProduto;
        this.valor = valor;
        this.peso = peso;
        this.comprimento = comprimento;
        this.altura = altura;
        this.largura = largura;
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

    public double getPeso() {
        return peso;
    }

    public double getComprimento() {
        return comprimento;
    }

    public double getAltura() {
        return altura;
    }

    public double getLargura() {
        return largura;
    }

    public double getVolume() {
        return (comprimento * altura * largura) / 1_000_000.0;
    }
}
