package model;

import java.util.List;

public class Entrega {
    private int id;
    private Cliente remetente;
    private Cliente destinatario;
    private Endereco origem;
    private Endereco destino;
    private List<ItemEntrega> itens;
    private StatusEntrega status;

    public Entrega(int id, Cliente remetente, Cliente destinatario, Endereco origem, Endereco destino,
                   List<ItemEntrega> itens, StatusEntrega status) {
        this.id = id;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.origem = origem;
        this.destino = destino;
        this.itens = itens;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getRemetente() {
        return remetente;
    }

    public Cliente getDestinatario() {
        return destinatario;
    }

    public Endereco getOrigem() {
        return origem;
    }

    public Endereco getDestino() {
        return destino;
    }

    public List<ItemEntrega> getItens() {
        return itens;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public void setStatus(StatusEntrega status) {
        this.status = status;
    }
}
