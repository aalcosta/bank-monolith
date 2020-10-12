package com.example.currency.api;

import com.example.currency.domain.Currency;
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

@Controller
@Path("/api/currency")
public class CurrencyAPI {

    private final CurrencyService currencyService;

    @Autowired
    public CurrencyAPI(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/currency")
    public Response getAll() {
        return Response.ok(currencyService.findAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/currency/{acronym}")
    public Response get(@PathParam("acronym") String acronym) {
        return Response.ok(currencyService.findById(acronym)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/currency/{acronym}/quotation")
    public Response getQuotation(@PathParam("acronym") String acronym,
                                 @QueryParam("reference") String reference,
                                 @QueryParam("value") BigDecimal value) {
        final Optional<Currency> source = currencyService.findById(acronym);
        if (source.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        final Optional<Currency> destination = currencyService.findById(reference);
        if (destination.isEmpty()) {
            return Response.ok(NaN).build();
        }

        if (isNull(value)) {
            value = BigDecimal.ONE;
        }

        return Response.ok(currencyService.convert(value, acronym, reference)).build();
    }

    @DELETE
    @Path("/currency/{acronym}")
    public Response delete(@PathVariable("acronym") String acronym) {
        currencyService.delete(acronym);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/currency/{acronym}")
    public Response save(Currency currency) {
        currencyService.save(currency);
        return Response.ok().build();
    }

}
