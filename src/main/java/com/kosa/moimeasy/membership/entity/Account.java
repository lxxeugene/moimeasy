package com.kosa.moimeasy.membership.entity;

import com.kosa.moimeasy.common.entity.BaseEntity;
import com.kosa.moimeasy.user.entity.User;
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
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    // DB에는 object를 저장할 수 없다.
    // name 설정 시 JPA에서 User_ID로 매핑해준다.
    @ManyToOne // Account(Many) to User(One)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Double balance;
}
