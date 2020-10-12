package currency;

import utils.json.JsonUtils;
import utils.rest.RestHelper;
import utils.rest.RestResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CurrencyService {

    private final static String BASE_URL = "http://localhost:8086/admin/currency";

    public List<Currency> findAll() {
        String json = RestHelper.doGetRequest(BASE_URL).content;
        return JsonUtils.fromJsonArray(json, Currency.class);
    }

    public Optional<Currency> findByAcronym(String acronym) {
        RestResponse restResponse = RestHelper.doGetRequest(BASE_URL);
        return restResponse.isSuccess()
                ? Optional.of(JsonUtils.fromJson(restResponse.content, Currency.class))
                : Optional.empty();
    }

    public void saveExchangeRate(String acronym, BigDecimal rate) {
        Currency entity = new Currency(acronym, rate);
        RestHelper.doPutRequest(BASE_URL, entity);
    }

}
