package com.example.currency.api;

import com.example.currency.domain.Currency;
import com.example.currency.exception.UnsupportedCurrencyException;
import com.example.currency.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Optional;

import static java.lang.Double.NaN;
import static java.util.Objects.isNull;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.springframework.util.StringUtils.isEmpty;

@Controller
@Path("/currency")
public class CurrencyApi {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyApi(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(currencyService.findAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{acronym}")
    public Response get(@PathParam("acronym") String acronym) {
        final Optional<Currency> currency = currencyService.findById(acronym);
        return currency.isPresent()
                ? Response.ok(currency).build()
                : Response.status(NOT_FOUND).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{acronym}/quotation")
    public Response getQuotation(@PathParam("acronym") String acronym,
                                 @QueryParam("reference") String reference,
                                 @QueryParam("value") BigDecimal value) {
        final Optional<Currency> source = currencyService.findById(acronym);
        if (source.isEmpty()) {
            return Response.status(NOT_FOUND).build();
        }

        final Optional<Currency> destination = isEmpty(reference)
                ? Optional.of(currencyService.getBaseCurrency()) : currencyService.findById(reference);
        if (destination.isEmpty()) {
            return Response.ok(NaN).build();
        }

        if (isNull(value)) {
            value = BigDecimal.ONE;
        }

        return Response.ok(
                currencyService.convert(value, source.get().getConversionRate(), destination.get().getConversionRate())).build();
    }

    @DELETE
    @Path("/{acronym}")
    public Response delete(@PathParam("acronym") String acronym) {
        currencyService.delete(acronym);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{acronym}")
    public Response save(Currency currency) {
        currencyService.save(currency);
        return Response.ok().build();
    }

}
