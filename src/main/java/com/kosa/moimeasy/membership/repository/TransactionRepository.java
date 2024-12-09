package com.kosa.moimeasy.membership.repository;

import com.kosa.moimeasy.membership.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
