package com.kosa.moimeasy.user.repository;

import com.kosa.moimeasy.user.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}

