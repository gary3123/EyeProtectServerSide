package com.example.eyeprotext.account;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "account")
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long id;
    private String name;
    private String email;
    private String password;
    private String dor;

    public Account() {
    }

    public Account(Long id,
                   String name,
                   String email,
                   String password,
                   String dor) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.dor = dor;
    }

    public Account(String name,
                   String email,
                   String password,
                   String dor) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dor = dor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDor() {
        return dor;
    }

    public void setDor(String dor) {
        this.dor = dor;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", dor=" + dor +
                '}';
    }
}
