package org.poo.cb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CurrencyConverter {

    public static void main(String[] args) {

    }


    private static final double[][] exchangeRates = {
            // Base, EUR, GBP, JPY, CAD, USD
            {1.0, 0.88, 129.7, 1.47, 1.59}, // EUR
            {1.14, 1.0, 147.2, 1.67, 1.81}, // GBP
            {0.0077, 0.0068, 1.0, 0.0113, 0.0123}, // JPY
            {0.68, 0.6, 88.5, 1.0, 1.09}, // CAD
            {0.91, 0.55, 81.3, 0.92, 1.0} // USD
    };


    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        int fromIndex = getIndex(fromCurrency);
        int toIndex = getIndex(toCurrency);

        if (fromIndex == -1 || toIndex == -1) {
            return -1;
        }

        return amount * exchangeRates[toIndex][fromIndex];
    }

    private static int getIndex(String currency) {
        switch (currency) {
            case "EUR":
                return 0;
            case "GBP":
                return 1;
            case "JPY":
                return 2;
            case "CAD":
                return 3;
            case "USD":
                return 4;
            default:
                return -1;
        }
    }


}

