package com.example.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
public class Person {

    @Id
    String id;
    String name;
    LocalDateTime birthday;
    String countryOfBirth;
    String address1;
    String address2;
    String country;
    String phone;
    String email;

}
