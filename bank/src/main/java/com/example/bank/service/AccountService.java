package com.example.bank.service;

import com.example.bank.domain.Account;
import com.example.bank.domain.Currency;
import com.example.bank.domain.CurrencyValue;
import com.example.bank.domain.Person;
import com.example.bank.exception.AccountNumberNotFoundException;
import com.example.bank.exception.InsufficientFoundsException;
import com.example.bank.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Path("admin")
public class AccountService {

    private final CurrencyService currencyService;

    private final AccountRepository accountRepo;

    @Autowired
    public AccountService(CurrencyService currencyService,
                          AccountRepository accountRepository) {
        this.currencyService = currencyService;
        this.accountRepo = accountRepository;
    }

    public Account createAccount(Person owner) {
        Currency eur = new Currency("EUR", BigDecimal.ONE);
        Account account = Account.builder()
                .number(newAccountNumber(owner))
                .owner(owner)
                .balance(BigDecimal.ZERO)
                .currency(eur)
                .build();
        return accountRepo.save(account);
    }

    public List<Account> findAll() {
        return accountRepo.findAll();
    }

    public Account getAccount(String accountNumber) throws AccountNumberNotFoundException {
        return accountRepo.findById(accountNumber).orElseThrow(() -> new AccountNumberNotFoundException());
    }

    @Transactional
    public void deposit(String accountNumber, CurrencyValue movementValue) {
        Account account = getAccount(accountNumber);
        BigDecimal amount = currencyService.convert(movementValue, account.getCurrency().getAcronym()).getValue();
        account.addBalance(amount);
        accountRepo.save(account);
    }

    @Transactional
    public void withdraw(String accountNumber, CurrencyValue movementValue) throws InsufficientFoundsException {
        Account account = getAccount(accountNumber);
        BigDecimal amount = currencyService.convert(movementValue, account.getCurrency().getAcronym()).getValue();

        final BigDecimal accountBalance = account.getBalance();
        if (accountBalance.compareTo(amount) < 0) {
            throw new InsufficientFoundsException();
        }

        account.subtractBalance(amount);
        accountRepo.save(account);
    }

    @Transactional
    public void transfer(String sourceAccount, String destinationAccount, CurrencyValue movementValue) {
        withdraw(sourceAccount, movementValue);
        deposit(destinationAccount, movementValue);
    }

    private String newAccountNumber(Person person) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

}
