package org.example.baedalteam27.domain.category.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.dto.request.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.response.CategoryResponseDto;
import org.example.baedalteam27.domain.category.dto.response.FindCategoriesResponseDto;
import org.example.baedalteam27.domain.category.dto.request.UpdateCategoryRequestDto;
import org.example.baedalteam27.domain.category.service.CategoryService;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // 카테고리 생성
    @PostMapping
    public ResponseEntity<CategoryResponseDto> saveCategory (
            @RequestBody CategoryRequestDto requestDto,
            @LoginUser Long userId) {
        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(requestDto, userId);
        return ResponseEntity.ok(categoryResponseDto);
    }

    // 카테고리 조회
    @GetMapping
    public ResponseEntity<List<FindCategoriesResponseDto>> findCategories () {
        List<FindCategoriesResponseDto> categories = categoryService.findCategories();
        return ResponseEntity.ok(categories);
    }

    // 카테고리 수정
    @PatchMapping("/{categoryid}")
    public ResponseEntity<Void> updateCategory (
            @PathVariable Long categoryid,
            @RequestBody UpdateCategoryRequestDto requestDto,
            @LoginUser Long userId) {
        categoryService.updateCategory(categoryid, requestDto, userId);
        return ResponseEntity.ok().build();
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryid}")
    public ResponseEntity<Void> deleteCategory (
            @PathVariable Long categoryid,
            @LoginUser Long userId) {
        categoryService.deleteCategory(categoryid, userId);
        return ResponseEntity.ok().build();
    }
}
