package com.kosa.moimeasy.user.entity;

import com.kosa.moimeasy.transaction.entity.Transaction;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(length = 255)
    private String address;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY) // Role과의 관계 설정
    @JoinColumn(name = "role_id", nullable = false) // 외래 키 매핑
    private Role role;

    @Column(length = 25)
    private String nickname;

    @Column(nullable = true)
    private Long moeimId;

    @Column
    private String profileImage;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        if(this.createAt == null){
            this.createAt = LocalDateTime.now();
        }else {
            this.createAt = createAt;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    @Column(name = "user_account_number", nullable = false) // 10자리 자동 생성
    private String accountNumber;

//    @Column(name = "user_account_password", nullable = false) // 유효성 검사 진행
//    private String accountPassword;

    // 기본 값을 0으로 설정
    @Column(name = "user_account_amount", nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private double amount = 0.0;

    // 거래내역 테이블
    @OneToMany(mappedBy = "userAccount", fetch = FetchType.LAZY)
    private List<Transaction> transactionSample = new ArrayList<>();

}



