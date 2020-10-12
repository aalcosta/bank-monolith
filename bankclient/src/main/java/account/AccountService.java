package account;

import currency.CurrencyValue;
import utils.json.JsonUtils;
import utils.rest.RestHelper;
import utils.rest.RestResponse;

import javax.ws.rs.core.HttpHeaders;
import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private final static String BASE_URL = "http://localhost:8086/admin/account";

    public String createAccount(Person person) {
        final RestResponse restResponse = RestHelper.doPostRequest(BASE_URL, person);

        String[] uriParts = restResponse.headers.get(HttpHeaders.LOCATION).split("/");
        return uriParts[uriParts.length - 1];
    }

    public List<Account> findAccounts() {
        String json = RestHelper.doGetRequest(BASE_URL).content;
        return JsonUtils.fromJsonArray(json, Account.class);
    }

    public Account deposit(String accountNumber, String currency, BigDecimal value) {
        CurrencyValue entity = new CurrencyValue(currency, value);
        String json = RestHelper.doPostRequest(BASE_URL + "/" + accountNumber + "/deposit", entity).content;
        return JsonUtils.fromJson(json, Account.class);
    }

    public Account withdraw(String accountNumber, String currency, BigDecimal value) {
        CurrencyValue entity = new CurrencyValue(currency, value);
        String json = RestHelper.doPostRequest(BASE_URL + "/" + accountNumber + "/withdraw", entity).content;
        return JsonUtils.fromJson(json, Account.class);
    }

    public void transfer(String originAccountNum, String destinationAccountNum, String currency, BigDecimal value) {
        AccountMovement entity = new AccountMovement(originAccountNum, destinationAccountNum, currency, value);
        RestHelper.doPostRequest(BASE_URL + "/transfer", entity);
    }
}

