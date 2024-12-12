package com.kosa.moimeasy.moeim.entity;

import com.kosa.moimeasy.transaction.entity.Transaction;
import com.kosa.moimeasy.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Moeim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moeimId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user; // User 타입으로 변경

    @Column(nullable = false, length = 100)
    private String moeimName;

    @Column(nullable = false, length = 50, unique = true)
    private String moeimCode;

    @Column(name = "moeim_account_number", nullable = false) // 길이 지정해야됌
    private String accountNumber;

    @Column(name = "moeim_account_balance", nullable = false)
    private double balance; // 오차 방지를 위해 Double 대신 BigDecimal 사용

    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "UPDATE_AT")
    private LocalDateTime updateAt;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        this.moeimCode = generateRandomCode(); // 6자리 랜덤 코드 생성
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    private String generateRandomCode() {
        return String.format("%06d", (int) (Math.random() * 1000000)); // 6자리 랜덤 숫자
    }

    // 거래내역 테이블
    // Moeim 엔티티를 저장하면 Transaction 엔티티들도 자동으로 저장된다. (삭제도 마찬가지)
    @OneToMany(mappedBy = "moeim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionList = new ArrayList<>();

    // 모임 계좌에 입금
    public void deposit(double balance){
        this.balance += balance;
    }

    // 모임 계좌에서 출금
    public void withdraw(double balance){
        if(this.balance< balance){
            throw new IllegalArgumentException("잔액 부족");
        }
        this.balance -= balance;
    }

}
