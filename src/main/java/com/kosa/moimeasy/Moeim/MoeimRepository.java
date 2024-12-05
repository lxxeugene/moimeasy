package com.kosa.moimeasy.Moeim;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoeimRepository extends JpaRepository<Moeim, Long> {
    Optional<Moeim> findById(Long moeimId);
}
