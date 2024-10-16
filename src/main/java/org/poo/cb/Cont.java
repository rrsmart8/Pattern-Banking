package org.poo.cb;

public class Cont {

    private String tipValuta;
    private double suma;

    public Cont(String tipValuta, double suma) {
        this.tipValuta = tipValuta;
        this.suma = suma;
    }

    public String getTipValuta() {
        return tipValuta;
    }

    public double getSuma() {
        return suma;
    }

    public void setSuma(double suma) {
        this.suma = suma;
    }

    public void adaugaSuma(double suma) {
        this.suma += suma;
    }

}
