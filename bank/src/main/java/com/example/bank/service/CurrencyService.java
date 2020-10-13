package com.example.bank.service;

import com.example.bank.domain.Currency;
import com.example.bank.domain.CurrencyValue;
import com.example.bank.exception.UnsupportedCurrencyException;
import com.example.bank.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CurrencyService {

    private static final Currency baseCurrency = new Currency("EUR", BigDecimal.ONE);

    private final CurrencyRepository repo;

    @Autowired
    public CurrencyService(CurrencyRepository currencyRepository) {
        this.repo = currencyRepository;
    }

    public List<Currency> findAll() {
        return repo.findAll();
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public CurrencyValue convert(CurrencyValue source, String targetCurrency) throws UnsupportedCurrencyException {
        if (source.getAcronym().equals(targetCurrency)) {
            return source;
        }

        BigDecimal convertedValue = convert(source.getValue(), source.getAcronym(), targetCurrency);
        return CurrencyValue.builder()
                .acronym(targetCurrency)
                .value(convertedValue)
                .build();
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

    public void save(Currency currency) {
        repo.save(currency);
    }
}
