package controller;


import model.ItemEntrega;

import java.util.List;

public class Frete {
    public static final double TAXA_BASE = 15.0;
    public static final double TAXA_POR_VOLUME = 0.8;

    public static double calculular (List<ItemEntrega>itens) {
        int quantidade = 0;
        if (itens != null)  {
            for (ItemEntrega item : itens) {
                quantidade += item.getQuantidade();
            }
        }
        return TAXA_BASE + TAXA_POR_VOLUME * quantidade;
    }
}
