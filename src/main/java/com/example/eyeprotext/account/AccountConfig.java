package com.example.eyeprotext.account;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class AccountConfig {

    @Bean
    CommandLineRunner commandLineRunner(AccountRepository repository) {
        return args -> {
            Account Gary = new Account(
                    1L,
                    "Gary",
                    "ban103123@gmail.com",
                    "ahri28692845",
                    "2023-7-27"
            );

            repository.saveAll(
                    List.of(Gary)
            );
        };
    }
}
