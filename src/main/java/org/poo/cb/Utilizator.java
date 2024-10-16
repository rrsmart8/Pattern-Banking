package org.poo.cb;

import java.util.ArrayList;
import java.util.Arrays;

public class Utilizator {

    private String email;
    private String nume;
    private String prenume;
    private String adresa;

    private boolean isPremium;

    private ArrayList<Cont> conturi = new ArrayList<>();
    private ArrayList<Actiuni> actiuni = new ArrayList<>();
    private ArrayList<Utilizator> prieteni = new ArrayList<>();

    private Utilizator(UtilizatorBuilder builder) {
        this.email = builder.email;
        this.nume = builder.nume;
        this.prenume = builder.prenume;
        this.adresa = builder.adresa;
        this.isPremium = builder.isPremium;
    }

    public String getPortfolioAsString() {
        StringBuilder portfolioString = new StringBuilder();

        // Append stocks
        portfolioString.append("{\"stocks\":[");

        for (Actiuni stock : actiuni) {
            portfolioString.append("{\"stockname\":\"").append(stock.getNumeCompanie())
                    .append("\",\"amount\":").append(stock.getAmount()).append("},");
        }
        if (!actiuni.isEmpty()) {
            portfolioString.setLength(portfolioString.length() - 1); // Remove the last comma
        }
        portfolioString.append("],");

        // Append accounts
        portfolioString.append("\"accounts\":[");

        for (Cont account : conturi) {
            String amount = String.format("%.2f", account.getSuma());
            portfolioString.append("{\"currencyname\":\"").append(account.getTipValuta())
                    .append("\",\"amount\":\"").append(amount).append("\"},");
        }
        if (!conturi.isEmpty()) {
            portfolioString.setLength(portfolioString.length() - 1); // Remove the last comma
        }

        portfolioString.append("]}]");

        return portfolioString.toString();
    }

    public String getPrenume() {
        return prenume;
    }

    public String getNume() {
        return nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public ArrayList<Cont> getConturi() {
        return conturi;
    }

    public ArrayList<Actiuni> getActiuni() {
        return actiuni;
    }

    public ArrayList<Utilizator> getPrieteni() {
        return prieteni;
    }

    public String getEmail() {
        return email;
    }

    public void addCont(Cont cont) {
        conturi.add(cont);
    }

    public void addActiuni(Actiuni actiune) {
        actiuni.add(actiune);
    }

    public void addPrieteni(Utilizator prieten) {
        prieteni.add(prieten);
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        this.isPremium = premium;
    }

    @Override
    public String toString() {
        // Iterați peste lista de prieteni și adăugați-i într-un șir de caractere
        String prieteniString = "";
        for (Utilizator prieten : prieteni) {
            prieteniString += "\"" + prieten.getEmail() + "\", ";
        }
        // Remove the last comma and space
        if (prieteniString.length() > 0) {
            prieteniString = prieteniString.substring(0, prieteniString.length() - 2);
        }

        return "{" +
                "\"email\": \"" + email + "\"," +
                "\"firstname\": \"" + prenume + "\"," +
                "\"lastname\": \"" + nume + "\"," +
                "\"address\": \"" + adresa + "\"," +
                "\"friends\": [" + prieteniString + "]" +
                "}";
    }

    public static class UtilizatorBuilder {

        // required parameters
        private String email;
        private String nume;
        private String prenume;
        private String adresa;

        // optional parameters
        private boolean isPremium;

        public UtilizatorBuilder(String email, String prenume, String nume, String adresa) {
            this.email = email;
            this.nume = nume;
            this.prenume = prenume;
            this.adresa = adresa;
        }

        public UtilizatorBuilder setPremium(boolean premium) {
            this.isPremium = premium;
            return this;
        }

        public Utilizator build() {
            return new Utilizator(this);
        }
    }
}
