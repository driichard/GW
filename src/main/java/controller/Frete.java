package controller;


import model.ItemEntrega;

import java.util.List;

public class Frete {
    public static final double TAXA_BASE = 15.0;
    public static final double TAXA_POR_VOLUME = 0.8;
    public static final double TAXA_POR_PESO = 1.2;

    public static double calcularVolume(double comprimento, double altura, double largura) {
        return (comprimento * altura * largura) / 1_000_000.0;
    }

    public static double calcular (double volume, double peso) {
        return TAXA_BASE + TAXA_POR_VOLUME * volume + TAXA_POR_PESO * peso;
    }

    public static double volumeTotal(List<ItemEntrega> itens) {
        double volume = 0;
        if (itens != null) {
            for (ItemEntrega item : itens) {
                volume += item.getProduto().getVolume() * item.getQuantidade();
            }
        }
        return volume;
    }

    public static double pesoTotal(List<ItemEntrega> itens) {
        double peso = 0;
        if (itens != null) {
            for (ItemEntrega item : itens) {
                peso += item.getProduto().getPeso() * item.getQuantidade();
            }
        }
        return peso;
    }

    public static double calcular(List<ItemEntrega> itens) {
        return calcular(volumeTotal(itens), pesoTotal(itens));
    }



}
