package com.kosa.moimeasy.user.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.kosa.moimeasy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    List<User> findByMoeimId(Long moeimId);

    // userId로 계좌 조회
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findAccountNumberById(@Param("userId") Long userId);

    // 입력한 계좌를 회원 테이블에서 조회
    @Query("SELECT u From User u WHERE u.accountNumber = :accountNumber")
    User findAccountNumber(@Param("accountNumber") String accountNumber);
}
