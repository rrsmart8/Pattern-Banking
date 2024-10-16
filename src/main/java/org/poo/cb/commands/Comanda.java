package org.poo.cb.commands;

import org.poo.cb.eBanking;

public abstract class Comanda {

    public abstract void executa(eBanking eBanking, String[] arguments);
}
