package org.example.baedalteam27.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.dto.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.CategoryResponseDto;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 카테고리 생성
    public CategoryResponseDto saveCategory(CategoryRequestDto requestDto) {
        Category category = new Category(requestDto.getName());
        Category saved = categoryRepository.save(category);
        return new CategoryResponseDto(saved.getId(), saved.getName());
    }



    // 카테고리 수정

    // 카테고리 삭제
}
