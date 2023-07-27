package com.example.eyeprotext;

import com.example.eyeprotext.account.Account;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class EyeProtextApplication {

    public static void main(String[] args) {
        SpringApplication.run(EyeProtextApplication.class, args);
    }

}
