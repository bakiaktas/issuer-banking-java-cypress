package com.baktas.issuerbanking.repository;

import com.baktas.issuerbanking.repository.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> { }