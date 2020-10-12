package currency;

import java.math.BigDecimal;

public class Currency {

    public String acronym;
    public BigDecimal conversionRate;

    public Currency() {
    }

    public Currency(String acronym, BigDecimal conversionRate) {
        this.acronym = acronym;
        this.conversionRate = conversionRate;
    }
}
