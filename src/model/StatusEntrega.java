package model;

public enum StatusEntrega {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    ENTREGUE("Entregue"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusEntrega(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
