package com.kosa.moimeasy.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "CREATE_AT", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "UPDATE_AT")
    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('admin', 'user') DEFAULT 'user'")
    private Role role;

    @Column(length = 25)
    private String nickname;

    @Column
    private Long moeimId;

    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
        if (this.role == null) {
            this.role = Role.user;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    public enum Role {
        user,admin
    }

//    @Column(name = "PROFILE_URL")
//    private String profileUrl;
//
//    // 다대다 관계 설정
//    @ManyToMany
//    @JoinTable(
//            name = "USER_MOEIM",
//            joinColumns = @JoinColumn(name = "USER_ID"),
//            inverseJoinColumns = @JoinColumn(name = "MOEIM_ID")
//    )
//    private Set<Moeim> moeims = new HashSet<>();

}
