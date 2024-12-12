package com.kosa.moimeasy.transaction.repository;

import com.kosa.moimeasy.transaction.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // 계좌 id, 조회 기간을 정해서 가져오는 것
    List<TransactionEntity> findByAccountIdAndTransactedAtBetween(
        Long accountId, LocalDateTime startDate, LocalDateTime endDate
    );
}
