package model;

import java.util.List;

public class Entrega {
    private int identificador;
    private Clientes clientes;
    private List<ItemEntrega> itens;
    private double valorEntrega;
    private StatusEntrega status;

    public Entrega(int identificador, Clientes clientes, List<ItemEntrega> itens, double valorEntrega, StatusEntrega status) {
        this.identificador = identificador;
        this.clientes = clientes;
        this.itens = itens;
        this.valorEntrega = valorEntrega;
        this.status = status;
    }

   // public StatusEntrega status () {
   //     return status;
  //  }

    public int getIdentificador() {
        return identificador;
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


