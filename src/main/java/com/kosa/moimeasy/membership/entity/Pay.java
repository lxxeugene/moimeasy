package com.kosa.moimeasy.membership.entity;

import com.kosa.moimeasy.common.entity.BaseEntity;
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
public class Pay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private payType type;

    @Column(nullable = false)
    private Double money;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private payStatus status;

    private String memo;

    public enum payType {
        TYPE1,
        TYPE2
    }

    public enum payStatus {
        PAID,
        UNPAID,
        PENDING
    }
}
