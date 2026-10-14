package model;

public class ItemEntrega {
    private Produto produto;
    private int quantidade;
    private double valorUnitario;

    public ItemEntrega(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = produto.getValor();
    }

    public ItemEntrega(Produto produto, int quantidade, double valorUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    public Produto getProduto () {
      return produto;
    }

    public int getQuantidade () {
        return quantidade;
    }

    public double getValorUnitario () {
        return valorUnitario;
    }
}
