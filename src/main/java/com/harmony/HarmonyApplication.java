package com.harmony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableJpaAuditing
@SpringBootApplication
public class HarmonyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HarmonyApplication.class, args);
    }

}
