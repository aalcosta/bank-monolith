package com.example.currency.service;

import com.example.currency.domain.Currency;
import com.example.currency.exception.UnsupportedCurrencyException;
import com.example.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private static final Currency baseCurrency = new Currency("EUR", BigDecimal.ONE);

    private final CurrencyRepository repo;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.repo = currencyRepository;
    }

    public BigDecimal convert(BigDecimal sourceValue, String sourceCurrency, String targetCurrency) throws UnsupportedCurrencyException {
        Currency toBaseRate = repo.findById(sourceCurrency).orElseThrow(() -> new UnsupportedCurrencyException());
        Currency fromBaseRate = repo.findById(targetCurrency).orElseThrow(() -> new UnsupportedCurrencyException());
        return convert(sourceValue, toBaseRate.getConversionRate(), fromBaseRate.getConversionRate());
    }

    public BigDecimal convert(BigDecimal sourceValue, BigDecimal toBaseRate, BigDecimal fromBaseRate) {
        return sourceValue.multiply(toBaseRate).divide(fromBaseRate,2, RoundingMode.HALF_UP);
    }

    public List<Currency> findAll() {
        return repo.findAll();
    }

    public Optional<Currency> findById(String acronym) {
        return repo.findById(acronym);
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public Optional<Currency> delete(String acronym) {
        return repo.delete(acronym);
    }

    public void save(Currency currency) {
        repo.save(currency);
    }


}
