package currency;

import java.math.BigDecimal;

public class CurrencyValue {

    private String acronym;
    private BigDecimal value;

    public CurrencyValue() {};

    public CurrencyValue(String currency, BigDecimal value) {
        this.acronym = currency;
        this.value = value;
    }
}