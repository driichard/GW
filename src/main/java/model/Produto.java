package model;

public class Produto {
    private int id;
    private String nomeDoProduto;
    private double valor;
    private double peso;
    private double comprimento;
    private double altura;
    private double largura;
    private int estoque;

    public Produto(int id, String nomeDoProduto, double valor, double peso, double comprimento, double altura, double largura, int estoque) {
        this.id = id;
        this.nomeDoProduto = nomeDoProduto;
        this.valor = valor;
        this.peso = peso;
        this.comprimento = comprimento;
        this.altura = altura;
        this.largura = largura;
        this.estoque = estoque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getEstoque() {
        return estoque;
    }

    public double getVolume() {
        return (comprimento * altura * largura) / 1_000_000.0;
    }

    public String getTamanho() {
        return comprimento + " × " + altura + " × " + largura + " cm";
    }
}
