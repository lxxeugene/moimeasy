package com.kosa.moimeasy.membership.repository;

import com.kosa.moimeasy.membership.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
