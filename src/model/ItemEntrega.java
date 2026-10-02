package model;

public class ItemEntrega {
    private int id;
    private Produto produto;
    private int quantidade;
    private double valorUnitario;

    public ItemEntrega(int id, Produto produto, int quantidade, double valorUnitario) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }




}
