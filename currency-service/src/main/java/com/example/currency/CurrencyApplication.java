package com.example.currency;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class CurrencyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        new CurrencyApplication().configure(new SpringApplicationBuilder(CurrencyApplication.class)).run(args);
    }

}
