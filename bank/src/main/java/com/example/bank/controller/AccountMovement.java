package com.example.bank.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountMovement {

    private String originAccount;
    private String destinationAccount;
    private String currency;
    private BigDecimal value;

}
