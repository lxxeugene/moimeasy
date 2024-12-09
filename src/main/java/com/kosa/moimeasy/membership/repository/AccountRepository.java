package com.kosa.moimeasy.membership.repository;

import com.kosa.moimeasy.membership.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
