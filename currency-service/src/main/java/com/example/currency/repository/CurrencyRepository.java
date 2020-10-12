package com.example.currency.repository;

import com.example.currency.domain.Currency;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Repository
public class CurrencyRepository {

    private static Map<String, Currency> currencies;
    static {
        currencies = new TreeMap<>();
        currencies.put("EUR", new Currency("EUR", BigDecimal.ONE));
        currencies.put("GBP", new Currency("GBP", new BigDecimal(1.15)));
        currencies.put("USD", new Currency("USD", new BigDecimal(0.88)));
        currencies.put("BRL", new Currency("BRL", new BigDecimal(0.22)));
    }

    public List<Currency> findAll() {
        return currencies.values().stream().collect(Collectors.toList());
    }

    public Optional<Currency> findById(String id) {
        return Optional.ofNullable(currencies.get(id));
    }

    public Currency save(final Currency currency) {
        currencies.put(currency.getAcronym(), currency);
        return currency;
    }

    public Optional<Currency> delete(String acronym) {
        return Optional.ofNullable(currencies.remove(acronym));
    }
}
