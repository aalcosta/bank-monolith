package com.example.currency.service;

import com.example.currency.domain.Currency;
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
        BigDecimal toBaseRate = repo.findById(sourceCurrency)
                .map(Currency::getConversionRate)
                .orElseThrow(() -> new UnsupportedCurrencyException());
        BigDecimal fromBaseRate = repo.findById(targetCurrency)
                .map(Currency::getConversionRate)
                .orElseThrow(() -> new UnsupportedCurrencyException());
        return sourceValue.multiply(toBaseRate).divide(fromBaseRate).setScale(2, RoundingMode.HALF_UP);
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
