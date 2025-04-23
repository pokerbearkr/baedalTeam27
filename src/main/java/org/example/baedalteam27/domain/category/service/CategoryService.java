package org.example.baedalteam27.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.dto.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.CategoryResponseDto;
import org.example.baedalteam27.domain.category.dto.FindCategoriesResponseDto;
import org.example.baedalteam27.domain.category.dto.UpdateCategoryRequestDto;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    // 카테고리 조회
    public List<FindCategoriesResponseDto> findCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new FindCategoriesResponseDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    // 카테고리 수정
    public void updateCategory(Long categoryid, UpdateCategoryRequestDto requestDto) {
        Category category = categoryRepository.findCategoryById(categoryid)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        category.update(requestDto.getName());
    }

    // 카테고리 삭제
}
