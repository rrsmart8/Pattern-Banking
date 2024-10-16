package org.poo.cb;

import java.util.ArrayList;

public class Actiuni {

    private String numeCompanie;
    private ArrayList<Double> last10Prices;
    private int amount;

    public Actiuni(String numeCompanie, ArrayList<Double> last10Prices) {
        this.numeCompanie = numeCompanie;
        this.last10Prices = last10Prices;
        this.amount = 0;
    }

    public String getNumeCompanie() {
        return numeCompanie;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ArrayList<Double> getLast10Prices() {
        return last10Prices;
    }

    public void setLast10Prices(ArrayList<Double> last10Prices) {
        this.last10Prices = last10Prices;
    }

    public void adaugaPret(double pret) {
        last10Prices.add(pret);
    }

}
