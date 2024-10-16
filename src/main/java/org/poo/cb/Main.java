package org.poo.cb;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {

        if (args == null) {
            System.out.println("Running Main");
            return;
        }

        String resourcePath = "src/main/resources/";

        eBanking eBanking = null;
        eBanking = eBanking.instance();

            eBanking.setExchangeRatesFile(resourcePath + args[0]);
            eBanking.setStockValuesFile(resourcePath + args[1]);
            eBanking.setCommandsFile(resourcePath + args[2]);

            eBanking.readCommands();

            eBanking.deleteUsers();
            eBanking.deleteStocks();

    }
}