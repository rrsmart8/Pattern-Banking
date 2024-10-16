package org.poo.cb.commands;
import org.poo.cb.eBanking;

public class BuyPremium extends Comanda {

    public void executa(eBanking eBanking, String[] arguments) {
        try {
            eBanking.buyPremium(arguments[0]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
