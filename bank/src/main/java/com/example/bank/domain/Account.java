package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ZERO;
import static java.util.Objects.isNull;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class Account {

    @Id
    private String number;
    private Person owner;
    private BigDecimal balance = ZERO;
    private Currency currency;

    public void setBalance(BigDecimal value) {
        value = isNull(value) ? ZERO : value;
        this.balance = value.setScale(2, RoundingMode.HALF_UP);
    }

    @Transient
    public void addBalance(BigDecimal value) {
        this.setBalance(balance.add(value));
    }

    @Transient
    public void subtractBalance(BigDecimal value) {
        this.setBalance(balance.subtract(value));
    }

}
