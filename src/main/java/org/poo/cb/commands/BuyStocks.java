package org.poo.cb.commands;
import org.poo.cb.eBanking;

public class BuyStocks extends Comanda{

    public void executa(eBanking eBanking, String[] arguments) {
        try {
            eBanking.buyStocks(arguments[0], arguments[1], Integer.parseInt(arguments[2]));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
