package com.example.bank;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BankApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new BankApplication().configure(new SpringApplicationBuilder(BankApplication.class)).run(args);
    }

}
