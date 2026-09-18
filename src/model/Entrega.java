package model;

public class Entrega {
    private int id;
    private Cliente cliente;
    private Endereco endereco;
    private Produto produto;
    private double valorEntrega;
    private String status;

    public Entrega(int id, Cliente cliente, Endereco endereco, Produto produto, double valorEntrega, String status) {
        this.id = id;
        this.cliente = cliente;
        this.endereco = endereco;
        this.produto = produto;
        this.valorEntrega = valorEntrega;
        this.status = status;
    }

    public void iniciarEntrega (){

    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getValorEntrega() {
        return valorEntrega;
    }

    public void setValorEntrega(double valorEntrega) {
        this.valorEntrega = valorEntrega;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
