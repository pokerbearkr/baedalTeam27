package org.example.baedalteam27.domain.category.service;

import org.example.baedalteam27.domain.category.dto.request.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.request.UpdateCategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.response.CategoryResponseDto;
import org.example.baedalteam27.domain.category.dto.response.FindCategoriesResponseDto;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.example.baedalteam27.global.exception.CustomException;
import org.example.baedalteam27.global.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void saveCategory_성공() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.ADMIN, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        String name = "한식";
        Category category = new Category(name);
        ReflectionTestUtils.setField(category, "id", 1L);
        CategoryRequestDto dto = new CategoryRequestDto(name);
        given(categoryRepository.save(any(Category.class))).willReturn(category);

        // when
        CategoryResponseDto categoryResponseDto = categoryService.saveCategory(dto, userId);

        // then
        assertThat(categoryResponseDto).isNotNull();
        assertThat(categoryResponseDto.getName()).isEqualTo(name);
        assertThat(categoryResponseDto.getId()).isEqualTo(1L);
    }

    @Test
    void saveCategory_USER인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.USER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);
        CategoryRequestDto dto = new CategoryRequestDto("한식");

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.saveCategory(dto, userId));
        assertEquals(ErrorCode.NOT_ADMIN, customException.getErrorCode());
    }

    @Test
    void saveCategory_OWNER인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);
        CategoryRequestDto dto = new CategoryRequestDto("한식");

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.saveCategory(dto, userId));
        assertEquals(ErrorCode.NOT_ADMIN, customException.getErrorCode());
    }

    @Test
    void findCategories_성공() {
        // given
        Category category = new Category("한식");
        Category category2 = new Category("일식");
        List<Category> categories = Arrays.asList(category, category2);
        given(categoryRepository.findAll()).willReturn(categories);

        // when
        List<FindCategoriesResponseDto> findCategoriesResponseDtos = categoryService.findCategories();

        // then
        assertEquals("한식", findCategoriesResponseDtos.get(0).getName());
        assertEquals("일식", findCategoriesResponseDtos.get(1).getName());
    }

    @Test
    void updateCategory_성공() {
        // given
        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto("양식");

        Long userId = 1L;
        User user = new User("email", "password", UserRole.ADMIN, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        // when
        categoryService.updateCategory(categoryId, dto, userId);

        // then
        assertEquals("양식", category.getName());
    }

    @Test
    void updateCategory_실패_USER인_경우() {
        // given
        Long categoryId = 1L;
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto("양식");

        Long userId = 1L;
        User user = new User("email", "password", UserRole.USER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.updateCategory(categoryId, dto, userId));
        assertEquals(ErrorCode.NOT_ADMIN, customException.getErrorCode());
    }

    @Test
    void updateCategory_실패_OWNER인_경우() {
        // given
        Long categoryId = 1L;
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto("양식");

        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.updateCategory(categoryId, dto, userId));
        assertEquals(ErrorCode.NOT_ADMIN, customException.getErrorCode());
    }

    @Test
    void updateCategory_실패_중복된_이름인_경우() {
        // given
        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto("한식");

        Long userId = 1L;
        User user = new User("email", "password", UserRole.ADMIN, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.updateCategory(categoryId, dto, userId));
        assertEquals(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS, customException.getErrorCode());
    }

    @Test
    void deleteCategory_성공() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.ADMIN, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);

        // when
        categoryService.deleteCategory(categoryId, userId);

        // then
        assertTrue(category.isDeleted());
    }

    @Test
    void deleteCategory_실패_USER인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.USER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.deleteCategory(categoryId, userId));
        assertEquals(ErrorCode.NOT_ADMIN, customException.getErrorCode());
    }

    @Test
    void deleteCategory_실패_OWNER인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> categoryService.deleteCategory(categoryId, userId));
        assertEquals(ErrorCode.NOT_ADMIN, customException.getErrorCode());
    }
}
