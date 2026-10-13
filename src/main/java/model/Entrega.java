package model;

import java.util.List;

public class Entrega {
    private int codigo;
    private Cliente remetente;
    private Cliente destinatario;
    private List<ItemEntrega> itens;
    private double valorEntrega;
    private StatusEntrega status;

    public Entrega(int codigo, Cliente remetente, Cliente destinatario, List<ItemEntrega> itens, double valorEntrega, StatusEntrega status) {
        this.codigo = codigo;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.itens = itens;
        this.valorEntrega = valorEntrega;
        this.status = status;
    }

    public int getCodigo() {
        return codigo;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public List<ItemEntrega> getItens() {
        return itens;
    }

    public double getValorEntrega() {
        return valorEntrega;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }

}
