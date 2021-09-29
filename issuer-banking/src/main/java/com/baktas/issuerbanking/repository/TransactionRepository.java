package com.baktas.issuerbanking.repository;

import com.baktas.issuerbanking.repository.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findByRefTransactionId(String refTransactionId);
}