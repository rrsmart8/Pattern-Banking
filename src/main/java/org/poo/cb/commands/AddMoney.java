package org.poo.cb.commands;
import org.poo.cb.eBanking;

public class AddMoney extends Comanda{

        public void executa(eBanking eBanking, String[] arguments) {
            try {
                eBanking.addMoney(arguments[0], arguments[1], Double.parseDouble(arguments[2]));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
}
