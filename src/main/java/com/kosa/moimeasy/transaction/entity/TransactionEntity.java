package com.kosa.moimeasy.transaction.entity;

import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.transaction.type.Transaction;
import com.kosa.moimeasy.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name = "TRANSACTION")
@EntityListeners(AuditingEntityListener.class)
public class TransactionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Moeim moeimAccount;

    @ManyToOne
    private User userAccount;

    @Enumerated(EnumType.STRING)
    private Transaction transactionType;

    @NotNull
    private Long amount;

    private String depositName; // 입금자명

    private String withdrawName; // 출금자명

    private String receivedName; // 송금받는 계좌주명

    private String receivedAccount; // 송금받는 계좌번호

    @CreatedDate
    private LocalDateTime transactedAt;
}
