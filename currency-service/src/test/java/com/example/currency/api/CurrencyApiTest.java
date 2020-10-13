package com.example.currency.api;

import com.example.currency.domain.Currency;
import com.example.currency.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

import static java.lang.Double.NaN;
import static java.math.BigDecimal.ONE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyApiTest {

    private static final Currency EURO = new Currency("EUR", ONE);
    private static final Currency DOLLAR = new Currency("USD", new BigDecimal(.75).setScale(2));
    private static final Currency REAL = new Currency("BRL", new BigDecimal(.15, MathContext.DECIMAL64).setScale(2));

    @LocalServerPort
    int port;

    @MockBean
    private CurrencyService currencyService;

    private WebTestClient webClient;

    @BeforeEach
    private void setUp() {
        webClient = WebTestClient
                .bindToServer()
                .baseUrl("http://localhost:" + port + "/currency")
                .build();
    }

    @Test
    void testGetAll() {
        when(currencyService.findAll()).thenReturn(List.of(EURO, DOLLAR, REAL));
        webClient.get().exchange()
                .expectStatus().isOk()
                .expectBodyList(Currency.class).contains(EURO, DOLLAR, REAL);
    }

    @Test
    void testGet() {
        when(currencyService.findById(EURO.getAcronym())).thenReturn(Optional.of(EURO));
        webClient.get().uri("/{acronym}", EURO.getAcronym()).exchange()
                .expectStatus().isOk()
                .expectBody(Currency.class).isEqualTo(EURO);
    }

    @Test
    void testGet_unknownCurrency() {
        when(currencyService.findById(any())).thenReturn(Optional.empty());
        webClient.get().uri("/{acronym}", EURO.getAcronym()).exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetQuotation() throws Exception {
        final BigDecimal paramValue = new BigDecimal("10.00");
        final BigDecimal convertedValue = new BigDecimal("7.50");

        when(currencyService.findById(EURO.getAcronym())).thenReturn(Optional.of(EURO));
        when(currencyService.findById(DOLLAR.getAcronym())).thenReturn(Optional.of(DOLLAR));
        when(currencyService.convert(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(convertedValue);
        webClient.get()
                .uri("/{acronym}/quotation?reference={reference}&value={value}",
                        DOLLAR.getAcronym(), EURO.getAcronym(), "10.00")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BigDecimal.class).isEqualTo(convertedValue);

        verify(currencyService).convert(eq(paramValue), eq(DOLLAR.getConversionRate()), eq(EURO.getConversionRate()));
    }

    @Test
    void testGetQuotation_unknownCurrency() {
        when(currencyService.findById(EURO.getAcronym())).thenReturn(Optional.of(EURO));
        when(currencyService.findById("UNKNOWN")).thenReturn(Optional.empty());
        webClient.get()
                .uri("/{acronym}/quotation", "UNKNOWN")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetQuotation_noReference() throws Exception {
        final BigDecimal paramValue = new BigDecimal("10.00");
        final BigDecimal convertedValue = new BigDecimal("7.50");

        when(currencyService.findById(DOLLAR.getAcronym())).thenReturn(Optional.of(DOLLAR));
        when(currencyService.getBaseCurrency()).thenReturn(EURO);
        when(currencyService.convert(any(BigDecimal.class), eq(DOLLAR.getConversionRate()), eq(EURO.getConversionRate()))).thenReturn(convertedValue);

        webClient.get()
                .uri("/{acronym}/quotation?value={value}",
                        DOLLAR.getAcronym(), "10.00")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BigDecimal.class).isEqualTo(convertedValue);
        verify(currencyService).getBaseCurrency();
        verify(currencyService).convert(eq(paramValue), eq(DOLLAR.getConversionRate()), eq(EURO.getConversionRate()));
    }

    @Test
    void testGetQuotation_unknownReference() {
        when(currencyService.findById(DOLLAR.getAcronym())).thenReturn(Optional.of(DOLLAR));
        when(currencyService.findById("UNKNOWN")).thenReturn(Optional.empty());
        webClient.get()
                .uri("/{acronym}/quotation?reference={UNKNOWN}",
                        DOLLAR.getAcronym(), "UNKNOWN")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Double.class).isEqualTo(NaN);
    }

    @Test
    void testGetQuotation_noValue() throws Exception {
        when(currencyService.findById(EURO.getAcronym())).thenReturn(Optional.of(EURO));
        when(currencyService.findById(DOLLAR.getAcronym())).thenReturn(Optional.of(DOLLAR));
        when(currencyService.convert(any(BigDecimal.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(DOLLAR.getConversionRate());

        webClient.get()
                .uri("/{acronym}/quotation?reference={reference}", DOLLAR.getAcronym(), EURO.getAcronym())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BigDecimal.class).isEqualTo(DOLLAR.getConversionRate());

        verify(currencyService).convert(eq(ONE), eq(DOLLAR.getConversionRate()), eq(EURO.getConversionRate()));
    }

    @Test
    void testDelete() {
        webClient.delete().uri("/{acronym}", REAL.getAcronym()).exchange()
                .expectStatus().isOk();
        verify(currencyService).delete(eq(REAL.getAcronym()));
    }

    @Test
    void testSave() {
        webClient.put().uri("/{acronym}", REAL.getAcronym())
                .contentType(APPLICATION_JSON)
                .body(just(REAL), Currency.class)
                .exchange()
                .expectStatus().isOk();
        verify(currencyService).save(eq(REAL));
    }
}