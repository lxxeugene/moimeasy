package com.kosa.moimeasy.moeim.repository;

import java.util.Optional;
import com.kosa.moimeasy.moeim.entity.Moeim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoeimRepository extends JpaRepository<Moeim, Long> {
    Optional<Moeim> findById(Long moeimId);
}
