package main.java.model;

import java.util.List;

public class Entrega {
    private int codigo;
    private Clientes clientes;
    private List<ItemEntrega> itens;
    private double valorEntrega;
    private StatusEntrega status;

    public Entrega(int codigo, Clientes clientes, List<ItemEntrega> itens, double valorEntrega, StatusEntrega status) {
        this.codigo = codigo;
        this.clientes = clientes;
        this.itens = itens;
        this.valorEntrega = valorEntrega;
        this.status = status;
    }

   // public StatusEntrega status () {
   //     return status;
  //  }

    public int getCodigo() {
        return codigo;
    }

    public Clientes getCliente() {
        return clientes;
    }

    public List<ItemEntrega> getItens() {
        return itens;
    }

    public double getValorEntrega() {
        return valorEntrega;
    }

    public StatusEntrega getStatus() {
        return status ;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

}


