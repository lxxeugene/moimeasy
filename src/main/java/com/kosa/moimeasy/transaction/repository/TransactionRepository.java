package com.kosa.moimeasy.transaction.repository;

import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.transaction.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // 모임 ID를 기반으로 거래 내역 조회
    List<Transaction> findByMoeim_Id(Long moeimId);

    // 모임 계좌에서 입금된 거래를 확인
    Optional<Transaction> findByMoeimAndDepositAmount(Moeim moeim, double depositAmount);

    // 모임 계좌번호에서 특정 시간 이후 거래 내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.moeim.accountNumber = :accountNumber " +
            "AND t.createAt >= :transactionAt ORDER BY t.createAt")
    List<Transaction> findByTransactionAtList(
            @Param("transactionAt") LocalDateTime transactionAt,
            @Param("accountNumber") String accountNumber
    );

    // 특정 모임 계좌에서 가장 최근의 입금 거래 조회
    Optional<Transaction> findFirstByMoeimAndDepositAmountOrderByCreateAtDesc(Moeim moeim, double depositAmount);

    // 특정 시간 이후 출금 거래 내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.moeim = :moeim " +
            "AND t.createAt >= :transactionAt AND t.withdrawalAmount > 0")
    List<Transaction> findByMoeimAndTransactionAtAfterAndWithdrawalAmountGreaterThan(
            @Param("moeim") Moeim moeim,
            @Param("transactionAt") LocalDateTime transactionAt
    );

    // 특정 계좌 번호의 거래를 시간 기준 내림차순으로 조회(페이지네이션)
    Page<Transaction> findByMoeim_AccountNumberOrderByCreateAtDesc(String accountNumber, Pageable pageable);

    // 특정 연도와 월에 해당하는 거래 내역 조회
    @Query("SELECT t FROM Transaction t WHERE t.moeim = :moeim " +
            "AND FUNCTION('YEAR', t.createAt) = :year AND FUNCTION('MONTH', t.createAt) = :month")
    List<Transaction> findByMoeimAndYearAndMonth(
            @Param("moeim") Long moeim,
            @Param("year") int year,
            @Param("month") int month
    );

    // 특정 기간 동안의 특정 거래 유형 조회
    List<Transaction> findByMoeimAccountNumberAndTransactionAtBetweenAndTransactionType(
            String accountNumber,
            LocalDateTime start,
            LocalDateTime end,
            int transactionType
    );

    Transaction findFirstByMoeim(Moeim moeimAccount);

    @Query("SELECT t FROM Transaction t WHERE t.moeim.moeimId = :moeimId")
    Page<Transaction> findByMoeim(@Param("moeimId") Long moeimId, Pageable pageable);

}
