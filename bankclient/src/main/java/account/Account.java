package account;

import currency.Currency;

import java.math.BigDecimal;

import static java.util.Objects.isNull;

public class Account {

    public String number;
    public Person owner;
    public Currency currency;
    public BigDecimal balance;

    @Override
    public String toString() {
        return "Account{number='" + number  + '\'' +
                ", owner='" + (isNull(owner) ? null : owner.getName()) + '\'' +
                ", currency='" + (isNull(currency) ? "EUR" : currency.acronym) + '\'' +
                ", balance=" + balance +
                '}';
    }
}
