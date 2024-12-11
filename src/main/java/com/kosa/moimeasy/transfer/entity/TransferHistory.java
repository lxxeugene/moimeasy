package com.kosa.moimeasy.transfer.entity;

import com.kosa.moimeasy.common.entity.BaseEntity;
import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TransferHistory extends BaseEntity { // 거래내역

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // (회원 계좌와 거래내역은 1:N 관계)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // (모임 계좌와 거래내역은 1:N 관계)
    @JoinColumn(name = "moeimId", nullable = false)
    private Moeim moeim;

    @Column(name = "transfer_amount", nullable = false)
    private BigDecimal transferAmount; //  이체 금액

    @Column(name = "transfer_After_Deposit", nullable = false)
    private BigDecimal amountAfterDeposit; //  거래 후 금액

    @Column(name = "transfer_After_Withdrawal", nullable = false)
    private BigDecimal amountAfterWithdrawal; //  인출 후 금액

    @Column(name = "transfer_memo", nullable = false)
    private String memo; //  메모

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type", nullable = false)
    private TransferType type;


    public enum TransferType{
        INCOME, // 수입
        EXPENSE // 지출
    }

    // 데이터베이스에 저장을 위한 생성자
    public TransferHistory(User user,
                           BigDecimal amountAfterWithdrawal,
                           String memo,
                           Moeim moeim,
                           BigDecimal amountAfterDeposit,
                           BigDecimal transferAmount,
                           TransferType transferType) {
        this.user = user;
        this.amountAfterWithdrawal = amountAfterWithdrawal;
        this.amountAfterDeposit = amountAfterDeposit;
        this.moeim = moeim;
        this.memo = memo;
        this.transferAmount = transferAmount;
        this.type = transferType;
    }
}
