package org.poo.cb.commands;
import org.poo.cb.eBanking;

public class ExchangeMoney extends Comanda{

    public void executa(eBanking eBanking, String[] arguments) {
        try {
            eBanking.exchangeMoney(arguments[0], arguments[1], arguments[2], Double.parseDouble(arguments[3]));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
