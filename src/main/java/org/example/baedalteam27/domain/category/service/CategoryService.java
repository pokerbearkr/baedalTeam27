package org.example.baedalteam27.domain.category.service;

import jakarta.transaction.Transactional;
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
        User user = userRepository.getUserByUserId(userId);
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
                .filter(category -> !category.isDeleted())  // isDeleted 가 false인 상태만 조회
                .map(category -> new FindCategoriesResponseDto(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    // 카테고리 수정
    @Transactional
    public void updateCategory(Long categoryid, UpdateCategoryRequestDto requestDto, Long userId) {
        User user = userRepository.getUserByUserId(userId);
        // 권한 확인
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("관리자 권한이 아닙니다.");
        }

        Category category = categoryRepository.findByIdOrElseThrow(categoryid);

        // 이름 중복 검증
        if (requestDto.getName().equals(category.getName())) {
            throw new IllegalArgumentException("중복된 이름입니다.");
        }

        // 삭제된 카테고리인지 검증
        isValidCategory(category);

        category.update(requestDto.getName());
    }

    // 카테고리 삭제
    public void deleteCategory(Long categoryid, Long userId) {
        User user = userRepository.getUserByUserId(userId);
        // 권한 확인
        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new ForbiddenException("관리자 권한이 아닙니다.");
        }

        Category category = categoryRepository.findByIdOrElseThrow(categoryid);

        // 삭제된 카테고리인지 검증
        isValidCategory(category);

        categoryRepository.delete(category);
    }

    // 삭제된 카테고리인지 검증
    public static void isValidCategory (Category category) {
        if (category.isDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 카테고리 입니다.");
        }
    }
}
