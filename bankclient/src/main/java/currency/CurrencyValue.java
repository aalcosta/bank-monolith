package currency;

import java.math.BigDecimal;

public class CurrencyValue {

    private String currency;
    private BigDecimal value;

    public CurrencyValue() {};

    public CurrencyValue(String currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }
}