package com.example.eyeprotext.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Configuration
public class AccountConfig {

    @Bean
    CommandLineRunner commandLineRunner(AccountRepository repository) {
        return args -> {
            Account Gary = new Account(
                    UUID.randomUUID(),
                    "Gary",
                    "aaa@gmail.com",
                    "aaa",
                    "2023-7-27",
                    "A6CACB2EA94019A9D62AEB80D24022F7765BEB51C2400D53671831D332A77845",
                    "",
                    new ArrayList<>()
            );

            repository.saveAll(
                    List.of(Gary)
            );
        };
    }
}
