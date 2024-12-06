package com.kosa.moimeasy.request.repository;

import com.kosa.moimeasy.request.entitry.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
