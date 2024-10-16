package org.poo.cb;

import org.poo.cb.Exceptions.*;
import org.poo.cb.commands.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class eBanking {

    private static eBanking instanceBank;

    private String commandsFile;
    private String exchangeRatesFile;
    private String stockValuesFile;

    private ArrayList<Utilizator> utilizatori = new ArrayList<Utilizator>();
    private ArrayList<Actiuni> actiuniDisponobile = new ArrayList<Actiuni>();

    private eBanking() {}

    public static eBanking instance() {
        if (instanceBank == null) {
            instanceBank = new eBanking();
        }
        return instanceBank;
    }

    public void deleteUsers(){
        utilizatori.clear();
    }

    public void deleteStocks(){
        actiuniDisponobile.clear();
    }

    public void createUser(String email, String nume, String prenume, String adresa) throws UserAlreadyExistsException {
        if (findUser(email) != null) {
            throw new UserAlreadyExistsException("User with " + email + " already exists");
        } else {
            Utilizator user = new Utilizator.UtilizatorBuilder(email, nume, prenume, adresa).build();
            user.setPremium(false);
            utilizatori.add(user);
        }
    }


    public void listUser(String email) throws UserNotFoundException {

        if (findUser(email) == null) {
            System.out.println("User with " + email + " doesn't exist");
        }

        for (Utilizator utilizator : utilizatori) {
            if (utilizator.getEmail().equals(email)) {
                System.out.println(utilizator.toString());
            }
        }
    }

    public void listPortofolio(String email) throws UserNotFoundException {

        if (findUser(email) == null) {
            System.out.println("User with " + email + " doesn't exist");
        }

        for (Utilizator utilizator : utilizatori) {
            if (utilizator.getEmail().equals(email)) {
                System.out.println(utilizator.getPortfolioAsString());
            }
        }
    }


    public void addFriend(String emailUser, String emailFriend) throws UserNotFoundException, UserAlreadyFriendException{
        try {
            Utilizator user = findUser(emailUser);
            Utilizator friend = findUser(emailFriend);

            if (user == null) {
                throw new UserNotFoundException("User with " + emailUser + " doesn't exist");
            }

            if (friend == null) {
                throw new UserNotFoundException("User with " + emailFriend + " doesn't exist");
            }

            if (user.getPrieteni().contains(friend)) {
                throw new UserAlreadyFriendException("User with " + emailFriend + " is already a friend");
            }

            user.getPrieteni().add(friend);
            friend.getPrieteni().add(user);

        } catch (UserNotFoundException  | UserAlreadyFriendException e) {
            System.out.println(e.getMessage());  // Handle user not found error
        }
    }

    public void addAccount(String email, String currency) throws UserAlreadyExistsException {
        Utilizator user = findUser(email);
        if (user != null) {
            if (findAccount(user, currency) != null) {
                throw new UserAlreadyExistsException("Account in currency " + currency + " already exists for user");
            }
            user.addCont(new Cont(currency, 0));
        }
    }

    public void addMoney(String email, String currency, double amount) {
        Utilizator user = findUser(email);
        Cont cont = findAccount(user, currency);
        if (cont != null) {
            cont.adaugaSuma(amount);
        }
    }

    private double[][] readExchangeRates(String filename) {

        double[][] exchangeRates = new double[5][5];

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            String line = reader.readLine();
            String[] firstLine = line.split(",");

            for (int i = 0; i < 5; i++) {
                exchangeRates[0][i] = Double.parseDouble(firstLine[i]);
            }

            for (int i = 1; i < 5; i++) {
                line = reader.readLine();
                String[] lineValues = line.split(",");
                for (int j = 0; j < 5; j++) {
                    exchangeRates[i][j] = Double.parseDouble(lineValues[j]);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return exchangeRates;
    }


    public void exchangeMoney(String email, String currencyFrom, String currencyTo, double money) throws InsufficentExchangeFunds {
        Utilizator user = findUser(email);
        Cont contFrom = findAccount(user, currencyFrom);
        Cont contTo = findAccount(user, currencyTo);

        if (user != null && contFrom != null && contTo != null) {
            double exchangeRate = CurrencyConverter.convertCurrency(1, currencyFrom, currencyTo);
            double wishedAmount = money * exchangeRate;

            if (wishedAmount > contFrom.getSuma())
                throw new InsufficentExchangeFunds("Insufficient amount in account " + currencyFrom + " for exchange");

            if (wishedAmount > 0.5 * contFrom.getSuma() && !user.isPremium()) {
                contFrom.setSuma(contFrom.getSuma() - (wishedAmount) * 0.01);
            }

            contFrom.setSuma(contFrom.getSuma() - money * exchangeRate);
            contTo.setSuma(contTo.getSuma() + money);

        }
    }

    public void transferMoney(String email, String emailFriend, String currency, double amount) throws InsufficentExchangeFunds, NotFriendsTransferExecption {
        Utilizator user = findUser(email);
        Utilizator friend = findUser(emailFriend);
        Cont cont = findAccount(user, currency);
        Cont contFriend = findAccount(friend, currency);

        if (areFriends(email, emailFriend) && cont != null && contFriend != null) {
            if (cont.getSuma() >= amount) {
                cont.setSuma(cont.getSuma() - amount);
                contFriend.setSuma(contFriend.getSuma() + amount);
            } else {
                throw new InsufficentExchangeFunds("Insufficient amount in account " + currency + " for transfer");
            }
        } else {
            throw new NotFriendsTransferExecption("You are not allowed to transfer money to " + emailFriend);
        }
    }

    public void buyStocks(String email, String company, int amount) throws InsufficentExchangeFunds{
        Utilizator user = findUser(email);

        Cont cont_dolari = findAccount(user, "USD");
        // Find the stock
        Actiuni stock = null;
        for (Actiuni actiune : actiuniDisponobile) {
            if (actiune.getNumeCompanie().equals(company)) {
                stock = actiune;
                break;
            }
        }

        // Get the first price from the last 10 prices
        double price = stock.getLast10Prices().get(stock.getLast10Prices().size() - 1);

        if (user.isPremium()) {
            price = price * 0.95;
        }

        if (cont_dolari.getSuma() < amount * price) {
            throw new InsufficentExchangeFunds("Insufficient amount in account for buying stock");
        }

        cont_dolari.setSuma(cont_dolari.getSuma() - amount * price);
        stock.setAmount(stock.getAmount() + amount);
        user.getActiuni().add(stock);

    }

    public void recommendStocks() {
        ArrayList<String> recommendedStocks = new ArrayList<>();

        for (Actiuni actiune : actiuniDisponobile) {
            // Mean of the last 5 prices
            double meanLast5 = 0;
            for (int i = 5; i < 10; i++) {
                meanLast5 += actiune.getLast10Prices().get(i);
            }
            meanLast5 = meanLast5 / 5.0;  // Divide by the number of periods

            // Mean of the last 10 prices
            double meanLast10 = 0;
            for (int i = 0; i < 10; i++) {
                meanLast10 += actiune.getLast10Prices().get(i);
            }
            meanLast10 = meanLast10 / 10.0;  // Divide by the number of periods

            if (meanLast5 > meanLast10) {
                recommendedStocks.add(actiune.getNumeCompanie());
            }
        }


        // Sort the recommended stocks by the name descending
        recommendedStocks.sort((s1, s2) -> s2.compareTo(s1));

        String recommendedStocksString = String.join("\", \"", recommendedStocks);

        String response = "{\"stockstobuy\":[\"" + recommendedStocksString + "\"]}";
        System.out.println(response);



    }



    private boolean areFriends(String email, String emailFriend) {
        Utilizator user = findUser(email);
        Utilizator friend = findUser(emailFriend);
        if (user.getPrieteni().contains(friend) && friend.getPrieteni().contains(user)) {
            return true;
        }
        return false;
    }

    private Cont findAccount(Utilizator user, String currency) {
        for (Cont cont : user.getConturi()) {
            if (cont.getTipValuta().equals(currency)) {
                return cont;
            }
        }
        return null;
    }

    private Utilizator findUser(String email) {
        for (Utilizator utilizator : utilizatori) {
            if (utilizator.getEmail().equals(email)) {
                return utilizator;
            }
        }
        return null;
    }

    private void getStocks() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(stockValuesFile));

            String line = reader.readLine();
            line = reader.readLine();

            while (line != null) {
                String stock = line.split(",")[0];

                ArrayList<Double> lastPrices = new ArrayList<Double>();
                for (int i = 1; i < 11; i++) {
                    lastPrices.add(Double.parseDouble(line.split(",")[i]));
                }

                actiuniDisponobile.add(new Actiuni(stock, lastPrices));
                line = reader.readLine();
            }

        } catch (IOException e) {
            System.out.println("FAILED TO READ STOCKS FILE");
        }

    }

    public void readCommands() {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(commandsFile));
            String line = reader.readLine();

            while (line != null) {

                // Create a string command with the first 2 words of the line
                String command = line.split(" ")[0] + " " + line.split(" ")[1];

                switch (command) {

                    case "CREATE USER":
                        String[] arguments = new String[4];
                        arguments[0] = line.split(" ")[2];
                        arguments[1] = line.split(" ")[3];
                        arguments[2] = line.split(" ")[4];
                        arguments[3] = String.join(" ", Arrays.copyOfRange(line.split(" "), 5, line.split(" ").length));

                        CreateUser createUser = new CreateUser();
                        createUser.executa(this, arguments);
                        break;
                    case "LIST USER":
                        ListUser listUser = new ListUser();
                        listUser.executa(this, new String[]{line.split(" ")[2]});
                        break;
                    case "ADD FRIEND":

                        String emailUser = line.split(" ")[2];
                        String emailFriend = line.split(" ")[3];

                        AddFriend addFriend = new AddFriend();
                        addFriend.executa(this, new String[]{emailUser, emailFriend});

                        break;
                    case "ADD ACCOUNT":

                        String emailAddAccount = line.split(" ")[2];
                        String currencyAddAccount = line.split(" ")[3];

                        AddAccount addAccount = new AddAccount();
                        addAccount.executa(this, new String[]{emailAddAccount, currencyAddAccount});

                        break;
                    case "ADD MONEY":

                        String emailAddMoney = line.split(" ")[2];
                        String currencyAddMoney = line.split(" ")[3];
                        double amountAddMoney = Double.parseDouble(line.split(" ")[4]);

                        AddMoney addMoney = new AddMoney();
                        addMoney.executa(this, new String[]{emailAddMoney, currencyAddMoney, String.valueOf(amountAddMoney)});

                        break;
                    case "EXCHANGE MONEY":

                        String emailExchangeMoney = line.split(" ")[2];
                        String currencyFrom = line.split(" ")[3];
                        String currencyTo = line.split(" ")[4];
                        double amountExchangeMoney = Double.parseDouble(line.split(" ")[5]);

                        ExchangeMoney exchangeMoney = new ExchangeMoney();
                        exchangeMoney.executa(this, new String[]{emailExchangeMoney, currencyFrom, currencyTo, String.valueOf(amountExchangeMoney)});

                        break;
                    case "TRANSFER MONEY":

                        String emailTransferMoney = line.split(" ")[2];
                        String emailFriendTransferMoney = line.split(" ")[3];
                        String currencyTransferMoney = line.split(" ")[4];
                        double amountTransferMoney = Double.parseDouble(line.split(" ")[5]);

                        TransferMoney transferMoney = new TransferMoney();
                        transferMoney.executa(this, new String[]{emailTransferMoney, emailFriendTransferMoney, currencyTransferMoney, String.valueOf(amountTransferMoney)});

                        break;
                    case "LIST PORTFOLIO":

                        String emailListPortfolio = line.split(" ")[2];

                        ListPortofolio listPortofolio = new ListPortofolio();
                        listPortofolio.executa(this, new String[]{emailListPortfolio});

                        break;
                    case "BUY STOCKS":

                        String emailBuyStocks = line.split(" ")[2];
                        String company = line.split(" ")[3];
                        int amount = Integer.parseInt(line.split(" ")[4]);

                        BuyStocks buyStocks = new BuyStocks();
                        buyStocks.executa(this, new String[]{emailBuyStocks, company, String.valueOf(amount)});

                        break;
                   case "RECOMMEND STOCKS":

                        RecommendStocks recommendStocks = new RecommendStocks();
                        recommendStocks.executa(this, new String[]{});

                        break;
                   case "BUY PREMIUM":

                        String emailBuyPremium = line.split(" ")[2];

                        BuyPremium buyPremium = new BuyPremium();
                        buyPremium.executa(this, new String[]{emailBuyPremium});

                        break;
                    default:
                        break;
                }

                line = reader.readLine();
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void buyPremium(String email) throws UserNotFoundException, InsufiicientMoneyPremiumException {

        Utilizator user = findUser(email);

        if (user == null) {
            throw new UserNotFoundException("User with " + email + " doesn't exist");
        }

        Cont contUsd = findAccount(user, "USD");

        if (contUsd.getSuma() < 100) {
            throw new InsufiicientMoneyPremiumException("Insufficient amount in account for buying premium option");
        }

        contUsd.setSuma(contUsd.getSuma() - 100);
        user.setPremium(true);

    }

    public String getCommandsFile() {
        return commandsFile;
    }

    public void setCommandsFile(String commandsFile) {
        this.commandsFile = commandsFile;
    }

    public String getExchangeRatesFile() {
        return exchangeRatesFile;
    }

    public void setExchangeRatesFile(String exchangeRatesFile) {
        this.exchangeRatesFile = exchangeRatesFile;
    }

    public String getStockValuesFile() {
        return stockValuesFile;
    }

    public void setStockValuesFile(String stockValuesFile) {
        this.stockValuesFile = stockValuesFile;
        getStocks();
    }

    public ArrayList<Utilizator> getUtilizatori() {
        return utilizatori;
    }

    public ArrayList<Actiuni> getActiuniDisponobile() {
        return actiuniDisponobile;
    }

    public void setUtilizatori(ArrayList<Utilizator> utilizatori) {
        this.utilizatori = utilizatori;
    }

    public void setActiuniDisponobile(ArrayList<Actiuni> actiuniDisponobile) {
        this.actiuniDisponobile = actiuniDisponobile;
    }

}
