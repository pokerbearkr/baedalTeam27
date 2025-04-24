package org.example.baedalteam27.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.category.dto.request.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.response.CategoryResponseDto;
import org.example.baedalteam27.domain.category.dto.response.FindCategoriesResponseDto;
import org.example.baedalteam27.domain.category.dto.request.UpdateCategoryRequestDto;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.example.baedalteam27.global.exception.ForbiddenException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // 카테고리 생성
    public CategoryResponseDto saveCategory(CategoryRequestDto requestDto, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        // 권한 확인
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("관리자 권한이 아닙니다.");
        }

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
    public void updateCategory(Long categoryid, UpdateCategoryRequestDto requestDto, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        // 권한 확인
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("관리자 권한이 아닙니다.");
        }

        Category category = categoryRepository.findCategoryById(categoryid)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        // 이름 중복 검증
        if (requestDto.getName().equals(category.getName())) {
            throw new IllegalArgumentException("중복된 이름입니다.");
        }

        category.update(requestDto.getName());
    }

    // 카테고리 삭제
    public void deleteCategory(Long categoryid, Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        // 권한 확인
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("관리자 권한이 아닙니다.");
        }

        Category category = categoryRepository.findCategoryById(categoryid)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        categoryRepository.delete(category);
    }
}
