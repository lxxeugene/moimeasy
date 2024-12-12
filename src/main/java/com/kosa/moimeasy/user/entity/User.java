package com.kosa.moimeasy.user.entity;

import com.kosa.moimeasy.common.entity.BaseEntity;
import com.kosa.moimeasy.transaction.entity.TransactionSample;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY) // Role과의 관계 설정
    @JoinColumn(name = "role_id", nullable = false) // 외래 키 매핑
    private Role role;

    @Column(length = 25)
    private String nickname;

    @Column
    private Long moeimId;

    @Column(length = 255, nullable = true)
    private String profileImage;

    @Column(name = "user_account_number", nullable = false) // 길이 지정해야됌
    private String accountNumber;

//    @Column(name = "user_account_password", nullable = false) // 유효성 검사 진행
//    private String accountPassword;

    @Column(name = "user_account_amount", nullable = false)
    private double amount;

    // 거래내역 테이블
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<TransactionSample> transactionSample = new ArrayList<>();

}



