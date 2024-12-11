package com.kosa.moimeasy.transfer.repository;

import com.kosa.moimeasy.transfer.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
