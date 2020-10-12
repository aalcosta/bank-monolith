package view;

import account.Person;
import currency.Currency;
import currency.CurrencyValue;
import utils.json.JsonUtils;
import utils.rest.RestHelper;
import utils.rest.RestResponse;

import javax.ws.rs.core.HttpHeaders;
import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;

public class InitialLoad {

    private final static String BASE_URL = "http://localhost:8086/admin";
    private final static String CURRENCY_URL = BASE_URL + "/currency";
    private final static String ACCOUNT_URL = BASE_URL + "/account";

    public static void main(String... args) {

        RestHelper.doPutRequest(CURRENCY_URL, new Currency("EUR", new BigDecimal("1.00")));
        RestHelper.doPutRequest(CURRENCY_URL, new Currency("USD", new BigDecimal("0.85")));
        RestHelper.doPutRequest(CURRENCY_URL, new Currency("BRL", new BigDecimal("0.18")));

        RestResponse restResponse = RestHelper.doGetRequest(CURRENCY_URL);
        List<Currency> currencies = JsonUtils.fromJsonArray(restResponse.content, Currency.class);
        currencies.forEach(c -> System.out.println(">>> " + c.acronym + " - " + c.conversionRate));

        restResponse = RestHelper.doPostRequest(ACCOUNT_URL,
                new Person().setName("Alexandre")
                        .setEmail("alexandre@codenomads.com")
                        .setAddress1("Huis Straat, 123, Den Haag")
                        .setCountry("NL")
        );

        final String url = restResponse.headers.get(HttpHeaders.LOCATION);
        final String[] urlParts = url.split("/");
        final String accountNum = urlParts[urlParts.length-1];
        System.out.println(">>> Account created: " + accountNum);

        RestHelper.doPostRequest(
                format("%s/%s/deposit", ACCOUNT_URL, accountNum),
                new CurrencyValue("EUR", BigDecimal.TEN)
        );

    }

}
