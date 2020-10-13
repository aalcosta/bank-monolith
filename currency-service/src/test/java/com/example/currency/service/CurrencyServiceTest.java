package com.example.currency.service;

import com.example.currency.domain.Currency;
import com.example.currency.exception.UnsupportedCurrencyException;
import com.example.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CurrencyServiceTest {

    private static final Currency EURO = new Currency("EUR", ONE);
    private static final Currency DOLAR = new Currency("USD", new BigDecimal(.75).setScale(2));

    private CurrencyRepository currencyRepository;

    private CurrencyService currencyService;

    @BeforeEach
    void setup() {
        currencyRepository = mock(CurrencyRepository.class);
        currencyService = new CurrencyService(currencyRepository);
    }

    @Test
    void testConvert_DOLAR_DOLAR() throws Exception {
        when(currencyRepository.findById(DOLAR.getAcronym())).thenReturn(Optional.of(DOLAR));

        final BigDecimal value = new BigDecimal("1.25");
        final BigDecimal quotation = currencyService.convert(value, DOLAR.getAcronym(), DOLAR.getAcronym());
        assertThat(quotation).isEqualTo(value);

    }

    @Test
    void testConvert_EURO_DOLAR() throws Exception {
        when(currencyRepository.findById(EURO.getAcronym())).thenReturn(Optional.of(EURO));
        when(currencyRepository.findById(DOLAR.getAcronym())).thenReturn(Optional.of(DOLAR));

        final BigDecimal quotation = currencyService.convert(TEN, EURO.getAcronym(), DOLAR.getAcronym());
        assertThat(quotation).isEqualTo(new BigDecimal("13.33"));
    }

    @Test
    void testConvert_DOLAR_EURO() throws Exception{
        when(currencyRepository.findById(EURO.getAcronym())).thenReturn(Optional.of(EURO));
        when(currencyRepository.findById(DOLAR.getAcronym())).thenReturn(Optional.of(DOLAR));

        final BigDecimal quotation = currencyService.convert(TEN, DOLAR.getAcronym(), EURO.getAcronym());
        assertThat(quotation).isEqualTo(new BigDecimal("7.50"));
    }

    @Test
    void testConvert_unknownSourceCurrency() {
        final String unknown = "UNK";
        final String euro = EURO.getAcronym();

        when(currencyRepository.findById(unknown)).thenReturn(Optional.empty());
        when(currencyRepository.findById(euro)).thenReturn(Optional.of(EURO));

        assertThrows(UnsupportedCurrencyException.class,() -> currencyService.convert(BigDecimal.TEN, unknown, euro));
    }

    @Test
    void testConvert_unknownTargetCurrency() {
        final String unknown = "UNK";
        final String euro = EURO.getAcronym();

        when(currencyRepository.findById(unknown)).thenReturn(Optional.empty());
        when(currencyRepository.findById(euro)).thenReturn(Optional.of(EURO));

        assertThrows(UnsupportedCurrencyException.class,() -> currencyService.convert(BigDecimal.TEN, euro, unknown));
    }

    @Test
    void testGetBaseCurrency() {
        assertThat(currencyService.getBaseCurrency()).isEqualTo(EURO);
    }

    @Test
    void testDelete() {
    }

    @Test
    void testSave() {
    }
}
