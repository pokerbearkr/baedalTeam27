package org.example.baedalteam27.domain.category.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.dto.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.CategoryResponseDto;
import org.example.baedalteam27.domain.category.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<CategoryResponseDto> saveCategory (@RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(requestDto);
        return ResponseEntity.ok(categoryResponseDto);
    }


    // 카테고리 삭제
}
