package com.example.bank.config;

import com.example.bank.controller.AdminController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(AdminController.class);
    }

}