package org.example.baedalteam27.domain.store.service;

import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.store.dto.request.SaveStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.response.SaveStoreResponseDto;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.example.baedalteam27.domain.user.UserRole;
import org.example.baedalteam27.domain.user.entitiy.User;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    void saveStore_성공() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        Store store = new Store("김밥집", "한국", "010", openTime, closedTime, 7000L, user, category);
        SaveStoreRequestDto dto = new SaveStoreRequestDto("김밥집", "한국", "010", openTime, closedTime, 7000L, categoryId);
        given(storeRepository.save(any(Store.class))).willReturn(store);

        // when
        SaveStoreResponseDto saveStoreResponseDto = storeService.saveStore(userId, dto);

        // then
        assertThat(saveStoreResponseDto).isNotNull();
        assertThat(saveStoreResponseDto.getStoreName()).isEqualTo("김밥집");
        assertThat(saveStoreResponseDto.getAddress()).isEqualTo("한국");
        assertThat(saveStoreResponseDto.getPhoneNumber()).isEqualTo("010");
        assertThat(saveStoreResponseDto.getOpenTime()).isEqualTo(openTime);
        assertThat(saveStoreResponseDto.getClosedTime()).isEqualTo(closedTime);
        assertThat(saveStoreResponseDto.getMinOrderPrice()).isEqualTo(7000L);
        assertThat(saveStoreResponseDto.getCategoryName()).isEqualTo(category.getName());
    }

    @Test
    void findStores() {
    }

    @Test
    void findStore() {
    }

    @Test
    void updateStore() {
    }

    @Test
    void deleteStore() {
    }
}