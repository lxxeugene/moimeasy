package com.kosa.moimeasy.user.repository;

import java.util.List;
import com.kosa.moimeasy.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.moeimId = :moeimId")
    List<User> findByMoeimId(@Param("moeimId") Long moeimId);

}
