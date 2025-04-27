package org.example.baedalteam27.domain.store.service;

import org.example.baedalteam27.domain.category.entity.Category;
import org.example.baedalteam27.domain.category.repository.CategoryRepository;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.store.dto.request.SaveStoreRequestDto;
import org.example.baedalteam27.domain.store.dto.response.SaveStoreResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreNameResponseDto;
import org.example.baedalteam27.domain.store.dto.response.StoreResponseDto;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.enums.Status;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
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
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
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
    void saveStore_실패_USER인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.USER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        SaveStoreRequestDto dto = new SaveStoreRequestDto("김밥집", "한국", "010", openTime, closedTime, 7000L, categoryId);

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> storeService.saveStore(userId, dto));
        assertEquals(ErrorCode.NOT_OWNER, customException.getErrorCode());
    }

    @Test
    void saveStore_실패_ADMIN인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.ADMIN, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        SaveStoreRequestDto dto = new SaveStoreRequestDto("김밥집", "한국", "010", openTime, closedTime, 7000L, categoryId);

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> storeService.saveStore(userId, dto));
        assertEquals(ErrorCode.NOT_OWNER, customException.getErrorCode());
    }

    @Test
    void saveStore_실패_소유한_가게의_갯수가_4개이상인_경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");
        given(categoryRepository.findByIdAndIsDeletedFalseOrElseThrow(categoryId)).willReturn(category);

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        SaveStoreRequestDto dto = new SaveStoreRequestDto("김밥집", "한국", "010", openTime, closedTime, 7000L, categoryId);
        given(storeRepository.countByUserIdAndIsDeletedFalse(userId)).willReturn(4);

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> storeService.saveStore(userId, dto));
        assertEquals(ErrorCode.STORE_LIMIT_EXCEPTION, customException.getErrorCode());
    }

    @Test
    void findStores_성공_카테고리Id로_조회() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        Store store1 = new Store("김밥집1", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store1, "id", 1L);
        Store store2 = new Store("김밥집2", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store2, "id", 2L);

        List<Store> storeList = Arrays.asList(store1, store2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "storeName"));
        Page<Store> stores = new PageImpl<>(storeList, pageable, storeList.size());
        given(storeRepository.findByCategoryIdAndIsDeletedFalse(eq(categoryId), any(Pageable.class))).willReturn(stores);

        // when
        Page<StoreNameResponseDto> storeNameResponseDtos = storeService.findStores(categoryId, null, pageable);

        // then
        assertEquals(1L, storeNameResponseDtos.getContent().get(0).getId());
        assertEquals("김밥집1", storeNameResponseDtos.getContent().get(0).getStoreName());
        assertEquals(2L, storeNameResponseDtos.getContent().get(1).getId());
        assertEquals("김밥집2", storeNameResponseDtos.getContent().get(1).getStoreName());
    }

    @Test
    void findStores_성공_가게이름으로_조회() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        Store store1 = new Store("김밥집1", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store1, "id", 1L);
        Store store2 = new Store("김밥집2", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store2, "id", 2L);

        List<Store> storeList = Arrays.asList(store1, store2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "storeName"));
        Page<Store> stores = new PageImpl<>(storeList, pageable, storeList.size());
        given(storeRepository.findByStoreNameContainingAndIsDeletedFalse(eq("김밥집"), any(Pageable.class))).willReturn(stores);

        // when
        Page<StoreNameResponseDto> storeNameResponseDtos = storeService.findStores(null, "김밥집", pageable);

        // then
        assertEquals(1L, storeNameResponseDtos.getContent().get(0).getId());
        assertEquals("김밥집1", storeNameResponseDtos.getContent().get(0).getStoreName());
        assertEquals(2L, storeNameResponseDtos.getContent().get(1).getId());
        assertEquals("김밥집2", storeNameResponseDtos.getContent().get(1).getStoreName());
    }

    @Test
    void findStores_성공_Param이_null인경우() {
        // given
        Long userId = 1L;
        User user = new User("email", "password", UserRole.OWNER, "", "");
        given(userRepository.getUserByUserId(userId)).willReturn(user);

        Long categoryId = 1L;
        Category category = new Category("한식");

        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        Store store1 = new Store("김밥집1", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store1, "id", 1L);
        Store store2 = new Store("김밥집2", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store2, "id", 2L);

        List<Store> storeList = Arrays.asList(store1, store2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "storeName"));
        Page<Store> stores = new PageImpl<>(storeList, pageable, storeList.size());
        given(storeRepository.findByIsDeletedFalse(any(Pageable.class))).willReturn(stores);

        // when
        Page<StoreNameResponseDto> storeNameResponseDtos = storeService.findStores(null, null, pageable);

        // then
        assertEquals(1L, storeNameResponseDtos.getContent().get(0).getId());
        assertEquals("김밥집1", storeNameResponseDtos.getContent().get(0).getStoreName());
        assertEquals(2L, storeNameResponseDtos.getContent().get(1).getId());
        assertEquals("김밥집2", storeNameResponseDtos.getContent().get(1).getStoreName());
    }

    @Test
    void findStores_살패_Param이_둘다입력받은경우() {
        // given
        Long categoryId = 1L;
        String storeName = "김밥집";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "storeName"));

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> storeService.findStores(categoryId, storeName, pageable));
        assertEquals(ErrorCode.INVALID_PARAMETER, customException.getErrorCode());
    }

    @Test
    void findStore_성공() {
        // given
        Category category = new Category("한식");

        User user = new User("email", "password", UserRole.USER, "", "");

        Long storeId = 1L;
        LocalTime openTime = LocalTime.parse("16:00:00");
        LocalTime closedTime = LocalTime.parse("22:00:00");
        Store store = new Store("김밥집", "한국", "010", openTime, closedTime, 7000L, user, category);
        ReflectionTestUtils.setField(store, "id", storeId);
        given(storeRepository.findByIdAndIsDeletedFalseOrElseThrow(storeId)).willReturn(store);

        Menu menu = new Menu(store, "김밥", 5000, "김, 햄, 시금치 등등", false);
        ReflectionTestUtils.setField(menu, "id", 1L);
        List<Menu> menus = Arrays.asList(menu);
        ReflectionTestUtils.setField(store, "menus", menus);

        Status currentStatus = store.getCurrentStatus(LocalTime.now());

        // when
        StoreResponseDto storeResponseDto = storeService.findStore(storeId);

        // then
        assertEquals(1L, storeResponseDto.getId());
        assertEquals("김밥집", storeResponseDto.getStoreName());
        assertEquals("한식", storeResponseDto.getCategoryName());
        assertEquals("한국", storeResponseDto.getAddress());
        assertEquals("010", storeResponseDto.getPhoneNumber());
        assertEquals(openTime, storeResponseDto.getOpenTime());
        assertEquals(closedTime, storeResponseDto.getClosedTime());
        assertEquals(7000L, storeResponseDto.getMinOrderPrice());
        assertEquals(currentStatus.toString(), storeResponseDto.getStatus());
        assertEquals("김밥", storeResponseDto.getMenus().get(0).getName());
        assertEquals(5000, storeResponseDto.getMenus().get(0).getPrice());
        assertEquals("김, 햄, 시금치 등등", storeResponseDto.getMenus().get(0).getDescription());
    }

    @Test
    void findStore_실패_storeId가_null인경우() {
        // given
        Long storeId = null;

        // when
        // then
        CustomException customException = assertThrows(CustomException.class, () -> storeService.findStore(storeId));
        assertEquals(ErrorCode.NULL_STORE_ID, customException.getErrorCode());
    }

    @Test
    void updateStore() {
    }

    @Test
    void deleteStore() {
    }
}