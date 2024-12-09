package com.kosa.moimeasy.membership.repository;

import com.kosa.moimeasy.membership.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayRepository extends JpaRepository<Pay, Long> {
}
