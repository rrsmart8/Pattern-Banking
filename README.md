# README for Pattern-Banking Project

## Overview
Pattern-Banking is a Java-based project that simulates a comprehensive **eBanking system** with various features including user management, account operations, currency exchange, stock purchases, and recommendations. The project showcases the use of various **design patterns** to efficiently model banking operations. It implements the **Singleton**, **Builder**, and **Command** patterns to achieve extensibility, modularity, and reusability of the code.

## Design Patterns Used

### 1. Singleton Pattern
- The **Singleton Pattern** is used to ensure that there is only one instance of the `eBanking` class throughout the application.
- This pattern is helpful in maintaining a consistent state of the application where shared resources (like user data) are managed centrally.

### 2. Builder Pattern
- The **Builder Pattern** is used in the `Utilizator` (User) class to create complex user objects step by step.
- This helps in providing an easy and readable way to create `Utilizator` objects with various optional parameters like premium status.

### 3. Command Pattern
- The **Command Pattern** is used to handle various user commands such as `CREATE USER`, `LIST USER`, `BUY STOCKS`, and more.
- Each command is encapsulated in a separate class (e.g., `CreateUser`, `AddFriend`, `BuyStocks`) which makes the application more modular and extensible.

## Features

- **User Management**: Create, delete, and list users. Add friends and manage user details.
- **Account Management**: Add different types of accounts for users, deposit money, and perform currency exchanges.
- **Stock Trading**: Buy and recommend stocks based on previous trends.
- **Currency Conversion**: Perform currency conversions using pre-defined exchange rates.
- **Premium Services**: Users can upgrade to premium status for additional benefits like reduced stock prices.

## Files and Structure

- **src/main/java/org/poo/cb/Main.java**: The entry point of the application. Initializes the `eBanking` instance and starts reading commands.
- **src/main/java/org/poo/cb/eBanking.java**: Implements the core banking operations using the **Singleton** pattern.
- **src/main/java/org/poo/cb/Utilizator.java**: Represents a user in the system using the **Builder** pattern for easy construction.
- **src/main/java/org/poo/cb/commands/**: Contains command classes like `CreateUser`, `ListUser`, `BuyStocks`, etc., implementing the **Command** pattern.
- **src/main/java/org/poo/cb/Cont.java**: Represents a user's account with different currencies.
- **src/main/java/org/poo/cb/Actiuni.java**: Represents stocks available for users to buy.
- **src/main/java/org/poo/cb/CurrencyConverter.java**: Contains exchange rates and functions to convert between different currencies.

## How to Build and Run

### Prerequisites

- **Java 8** or higher.
- **Maven** for build automation (optional).

### Building the Project

To build the project, navigate to the root directory of the project and run:

```sh
mvn clean install
```

This command will compile the Java code and package it into a runnable jar file.

### Running the Application

To run the `Pattern-Banking` project:

```sh
java -cp target/Pattern-Banking-1.0.jar org.poo.cb.Main <exchange_rates_file> <stock_values_file> <commands_file>
```

- `<exchange_rates_file>`: File containing exchange rates for different currencies.
- `<stock_values_file>`: File containing stock values.
- `<commands_file>`: File containing the list of commands to be executed.

### Command File Format
The command file should contain commands in a specific format:

- **CREATE USER** `<email>` `<first_name>` `<last_name>` `<address>`
- **ADD FRIEND** `<email_user>` `<email_friend>`
- **ADD ACCOUNT** `<email>` `<currency>`
- **ADD MONEY** `<email>` `<currency>` `<amount>`
- **EXCHANGE MONEY** `<email>` `<currency_from>` `<currency_to>` `<amount>`
- **BUY STOCKS** `<email>` `<company>` `<amount>`
- **RECOMMEND STOCKS**
- **BUY PREMIUM** `<email>`

## Example Commands File
```
CREATE USER user1@example.com John Doe "123 Main St"
ADD ACCOUNT user1@example.com USD
ADD MONEY user1@example.com USD 5000
BUY PREMIUM user1@example.com
LIST USER user1@example.com
```

## Features in Detail

### 1. User Management
- Users can be **created** with basic information like email, name, and address.
- Users can add **friends**, who are also users of the bank.
- The **Builder Pattern** simplifies user creation by enabling optional parameters.

### 2. Account Management
- Users can **add accounts** in different currencies.
- Users can **deposit money** and perform **currency exchanges** using the `CurrencyConverter` class.

### 3. Stock Trading
- Users can **buy stocks** from various companies.
- The system uses a recommendation mechanism that recommends stocks based on trends.
- Premium users enjoy **discounted rates** on stock purchases.

### 4. Premium Services
- Users can upgrade to **premium membership** for benefits like discounted stock prices.
- A premium account costs **100 USD**.

## Memory Management and Error Handling
- Proper error handling is implemented for cases like **user not found**, **insufficient funds**, or **already existing accounts**.
- The **Singleton** instance (`eBanking`) ensures only one shared state throughout the application.

## Future Improvements

- **Database Integration**: Currently, data is stored in memory. A future version could integrate a **database** for persistent storage.
- **Concurrency Handling**: Implement thread safety in critical sections, particularly for updating user accounts and stock operations.
- **REST API**: Expose banking operations as a **REST API** to support integration with web and mobile applications.


