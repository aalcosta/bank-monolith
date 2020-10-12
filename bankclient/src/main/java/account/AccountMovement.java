package account;

import java.math.BigDecimal;

public class AccountMovement {

    public String originAccount;
    public String destinationAccount;
    public String currency;
    public BigDecimal value;

    public AccountMovement() {
    }

    public AccountMovement(String originAccount, String destinationAccount, String currency, BigDecimal value) {
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.currency = currency;
        this.value = value;
    }
}
