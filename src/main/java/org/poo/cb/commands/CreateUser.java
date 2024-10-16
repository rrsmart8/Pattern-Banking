package org.poo.cb.commands;

import org.poo.cb.eBanking;

public class CreateUser extends Comanda {

    public void executa(eBanking eBanking, String[] arguments) {

        String email = arguments[0];
        String nume = arguments[1];
        String prenume = arguments[2];
        String adresa = arguments[3];

        try {
            eBanking.createUser(email, nume, prenume, adresa);
            } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
