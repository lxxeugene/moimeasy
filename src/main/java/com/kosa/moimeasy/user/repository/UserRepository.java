package com.kosa.moimeasy.user.repository;

import java.util.List;
import com.kosa.moimeasy.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    List<User> findByMoeimId(Long moeimId);
}
