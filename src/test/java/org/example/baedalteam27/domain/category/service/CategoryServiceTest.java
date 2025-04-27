package org.example.baedalteam27.domain.category.service;

import org.example.baedalteam27.domain.category.dto.request.CategoryRequestDto;
import org.example.baedalteam27.domain.category.dto.response.CategoryResponseDto;
import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
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
    void saveCategory_관리자_권한이_아닌_경우() {
    }

    @Test
    void findCategories() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}
