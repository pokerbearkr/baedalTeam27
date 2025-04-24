package org.example.baedalteam27.domain.category.repository;

import org.example.baedalteam27.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    default Category findByIdOrElseThrow(Long categoryid) {
        return findById(categoryid).orElseThrow(() -> new IllegalArgumentException("카테고리가 존재 하지 않습니다."));
    }
}
