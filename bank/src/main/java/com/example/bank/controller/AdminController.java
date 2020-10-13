package com.example.bank.controller;

import com.example.bank.domain.Account;
import com.example.bank.domain.Currency;
import com.example.bank.domain.CurrencyValue;
import com.example.bank.domain.Person;
import com.example.bank.exception.AccountNumberNotFoundException;
import com.example.bank.exception.InsufficientFoundsException;
import com.example.bank.repository.AccountRepository;
import com.example.bank.repository.CurrencyRepository;
import com.example.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.EXPECTATION_FAILED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Controller
@Path("/admin")
public class AdminController {

    private final AccountService accountService;
    private final AccountRepository accountRepo;
    private final CurrencyRepository currencyRepo;

    @Autowired
    public AdminController(AccountService accountService, AccountRepository accountRepository, CurrencyRepository currencyRepository) {
        this.accountService = accountService;
        this.accountRepo = accountRepository;
        this.currencyRepo = currencyRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/currency")
    public Response getCurrencies() {
        return Response.ok(currencyRepo.findAll()).build();
    }

    @DELETE
    @Path("/currency/{acronym}")
    public Response configureCurrency(@PathVariable("acronym") String acronym) {
        currencyRepo.deleteById(acronym);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/currency")
    public Response configureCurrency(Currency currency) {
        currencyRepo.save(currency);
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/account")
    public Response createAccount(@Context UriInfo uriInfo, Person person) {
        Account account = accountService.createAccount(person);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        final URI uri = builder.path(account.getNumber()).build();
        return Response.created(uri).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account")
    public Response getAccounts() {
        return Response.ok(accountService.findAll()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{accountNumber}")
    public Response getAccount(@PathParam("accountNumber") String accountNumber) {
        Optional<Account> account = accountRepo.findById(accountNumber);
        return account.isPresent()
                ? Response.ok(account.get()).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{accountNumber}/deposit")
    public Response deposit(@PathParam("accountNumber") String accountNumber, CurrencyValue currencyValue) {
        try {
            accountService.deposit(accountNumber, currencyValue);
            final Account account = accountService.getAccount(accountNumber);
            return Response.ok(account).build();
        } catch (AccountNumberNotFoundException e) {
            return Response.status(NOT_FOUND).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/account/{accountNumber}/withdraw")
    public Response withdraw(@PathParam("accountNumber") String accountNumber, CurrencyValue currencyValue) {
        try {
            accountService.withdraw(accountNumber, currencyValue);
            return Response.ok(accountService.getAccount(accountNumber)).build();
        } catch (AccountNumberNotFoundException e) {
            return Response.status(NOT_FOUND).build();
        } catch (InsufficientFoundsException e) {
            return Response.status(EXPECTATION_FAILED).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/account/transfer")
    public Response transfer(AccountMovement movement) {
        try {
            CurrencyValue currencyValue = CurrencyValue.builder().acronym(movement.getCurrency()).value(movement.getValue()).build();
            accountService.transfer(movement.getOriginAccount(), movement.getDestinationAccount(), currencyValue);
            return Response.ok().build();
        } catch (AccountNumberNotFoundException e) {
            return Response.status(NOT_FOUND).build();
        } catch (InsufficientFoundsException e) {
            return Response.status(EXPECTATION_FAILED).build();
        }
    }

}
