package com.example.currency.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
@AllArgsConstructor
public class Currency {

    String acronym;
    BigDecimal conversionRate;

}
