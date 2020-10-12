package com.example.currency.config;

import com.example.currency.api.CurrencyAPI;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(CurrencyAPI.class);
    }

}