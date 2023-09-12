package com.example.eyeprotext.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository
        extends JpaRepository<Account, UUID> {
    @Query ("SELECT a FROM Account a WHERE a.email = ?1")
    Optional<Account> findAccountByEmail(String email);

    @Query ("SELECT a FROM Account a WHERE a.email = ?1 AND a.password = ?2")
    Optional<Account> findAccountByEmailAndPassword(String email, String password);
}
