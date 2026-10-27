package model;

public class Produto {
    private int id;
    private String codigo;
    private String nomeDoProduto;
    private double valor;
    private double peso;
    private double comprimento;
    private double altura;
    private double largura;

    public Produto(int id, String codigo, String nomeDoProduto, double valor, double peso, double comprimento, double altura, double largura) {
        this.id = id;
        this.codigo = codigo;
        this.nomeDoProduto = nomeDoProduto;
        this.valor = valor;
        this.peso = peso;
        this.comprimento = comprimento;
        this.altura = altura;
        this.largura = largura;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo () {
        return codigo;
    }

    public void setCodigo (String codigo) {
        this.codigo = codigo;
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

    public String getTamanho() {
        return comprimento + " × " + altura + " × " + largura + " cm";
    }
}
