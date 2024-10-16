package org.poo.cb.commands;
import org.poo.cb.eBanking;

public class RecommendStocks extends Comanda{

    public void executa(eBanking eBanking, String[] arguments) {
        try {
            eBanking.recommendStocks();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
