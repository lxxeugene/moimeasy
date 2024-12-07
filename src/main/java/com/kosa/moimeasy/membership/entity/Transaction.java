package com.kosa.moimeasy.membership.entity;

import com.kosa.moimeasy.common.entity.BaseEntity;
import com.kosa.moimeasy.moeim.entity.Moeim;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TransactionId;

    @ManyToOne
    @JoinColumn(name = "MOEIM_ID", nullable = false)
    private Moeim moeim;

    @ManyToOne
    @JoinColumn(name = "Pay_Id", nullable = false)
    private Pay pay;

    @ManyToOne
    @JoinColumn(name = "Account_Id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private Double money;

    // 지출 : 0, 수입 1
    @Column(nullable = false)
    private boolean type;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

}
