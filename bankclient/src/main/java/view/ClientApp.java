package view;

import account.Account;
import account.AccountService;
import account.Person;
import currency.Currency;
import currency.CurrencyService;
import utils.ServiceFactory;

import java.io.Console;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;

public class ClientApp {

    private static CurrencyService currencyService = ServiceFactory.get(CurrencyService.class);
    private static AccountService accountService = ServiceFactory.get(AccountService.class);

    private static Console console = System.console();
    private final static Menu mainMenu = new Menu("Choose an option!")
            .addItem("Currencies", displayCurrencies())
            .addItem("Accounts", displayAccounts())
            .addItem("Exit", () -> console.printf("\nFarewell!"));
    private final static Menu currenciesMenu = new Menu("Currency Operations!")
            .addItem("List Currencies", displayCurrencies())
            .addItem("Define Currency Rate", defineCurrencyRate())
            .addItem("Exit", mainMenu::show);
    private final static Menu accountsMenu = new Menu("Account Operations!")
            .addItem("List Accounts", displayAccounts())
            .addItem("Create Account", createAccount())
            .addItem("Deposit", accountDeposit())
            .addItem("Withdraw", accountWithdraw())
            .addItem("Transfer", accountTransfer())
            .addItem("Exit", mainMenu::show);

    private static Runnable createAccount() {
        return () -> {
            try {
                console.printf("\n<<< Insert new Customer data! >>>\n");
                Person person = new Person()
                        .setName(console.readLine("Name: "))
                        .setEmail(console.readLine("Email: "))
                        .setAddress1(console.readLine("Address: "))
                        .setCountry(console.readLine("Country: "));

                String accountNumber = accountService.createAccount(person);

                console.printf("\nAccount '%s' created!!!\n", accountNumber);
            } catch (Exception e) {
                console.printf("Error creating account! Please try again later! ");
            }
            accountsMenu.show();
        };

    }

    private static Runnable accountDeposit() {
        return () -> {
            try {
                String accountNumber = console.readLine("Account: ");
                String currency = console.readLine("Currency: ").toUpperCase();
                BigDecimal value = new BigDecimal(console.readLine("Value:"));
                Account account = accountService.deposit(accountNumber, currency, value);

                console.printf("\nNew balance: %s %s\n", account.currency.acronym, account.balance);
            } catch (NumberFormatException e) {
                console.printf("\nInvalid Value!\n");
            } catch (Exception e) {
                console.printf("\nOperation can't be executed!\n");
            }
            accountsMenu.show();
        };

    }

    private static Runnable accountWithdraw() {
        return () -> {
            try {
                String accountNumber = console.readLine("Account: ");
                String currency = console.readLine("Currency:");
                BigDecimal value = new BigDecimal(console.readLine("Value:"));
                Account account = accountService.withdraw(accountNumber, currency, value);

                console.printf("\nNew balance: %s %s\n", account.currency, account.balance);
            } catch (NumberFormatException e) {
                console.printf("\nInvalid Value!\n");
            } catch (Exception e) {
                console.printf("\nOperation can't be executed!\n");
            }
            accountsMenu.show();
        };

    }

    private static Runnable accountTransfer() {
        return () -> {
            try {
                String originAccountNum = console.readLine("Origin Account: ");
                String destinationAccountNum = console.readLine("Destination Account: ");
                String currency = console.readLine("Currency:");
                BigDecimal value = new BigDecimal(console.readLine("Value:"));
                accountService.transfer(originAccountNum, destinationAccountNum, currency, value);

                console.printf("\nTransfer done!\n");
                displayAccounts().run();
            } catch (NumberFormatException e) {
                console.printf("\nInvalid Value!\n");
            } catch (Exception e) {
                console.printf("\nOperation can't be executed!\n");
            }
            accountsMenu.show();
        };
    }

    private static Runnable displayAccounts() {
        return () -> {
            console.printf("\n<<< Available accounts! >>>");
            accountService.findAccounts().stream().forEach(account -> console.printf("\n%s", account));
            accountsMenu.show();
        };
    }

    private static Runnable displayCurrencies() {
        return () -> {
            List<Currency> currencies = new CurrencyService().findAll();

            if (currencies.isEmpty()) {
                console.printf("\n<<<No Currencies Found! >>>");
            } else {
                console.printf("\n<<<Currencies - Exchange rate EUR based!>>>");
                Collections.sort(currencies, comparing(a -> a.acronym));
                currencies.forEach(currency ->
                        console.printf("\n  %s  -  %s", currency.acronym, currency.conversionRate)
                );
            }

            currenciesMenu.show();
        };
    }

    private static Runnable defineCurrencyRate() {
        return () -> {
            try {
                String acronym = console.readLine("\nCurrency: ");
                String rate = console.readLine("Rate: ");
                currencyService.saveExchangeRate(acronym, new BigDecimal(rate));
            } catch (NumberFormatException e) {
                console.printf("Invalid exchange rate!!! ");
            }
            currenciesMenu.show();
        };
    }

    public static void main(String... args) {
        mainMenu.show();
    }

}
