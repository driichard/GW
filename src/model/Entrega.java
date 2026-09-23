package model;

import service.Valores;

public class Entrega {
    private int id;
    private Clientes clientes;
    private Produto produto;
    private double valorEntrega;
    private String status;

    public Entrega(int id, Clientes clientes, Produto produto, double valorEntrega, String status) {
        this.id = id;
        this.clientes = clientes;
        this.produto = produto;
        this.valorEntrega = valorEntrega;
        this.status = status;
    }

    public double valorTotal (Valores valores) {
       return valores.calcularValorTotal(produto, this );
    }

    public String status () {
        return status;
    }

    public int getId() {
        return id;
    }

    public Clientes getCliente() {
        return clientes;
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
