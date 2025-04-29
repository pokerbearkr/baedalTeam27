package org.example.baedalteam27.domain.menu.service;

import org.example.baedalteam27.domain.menu.dto.MenuDto;
import org.example.baedalteam27.domain.menu.dto.MenuResponseDto;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.menu.repository.MenuRepository;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuService menuService;

    private Store store;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        store = Store.builder()
                .storeName("Test Store")
                .address("Test Address")
                .phoneNumber("010-1234-5678")
                .openTime(java.time.LocalTime.of(9, 0))
                .closedTime(java.time.LocalTime.of(22, 0))
                .minOrderPrice(10000L)
                .user(null) // 필요 시 MockUser 만들어서 넣어도 됨
                .category(null)
                .build();
    }

    @Test
    void createMenuTest() {
        // given
        MenuDto menuDto = new MenuDto(1L, "Test Menu", 12000, "Delicious test menu", false);
        Long storeId = 1L;
        Store store = Store.builder().storeName("ㅇㅇ").build();
        ReflectionTestUtils.setField(store, "id", storeId);
        Menu menu = Menu.builder()
                .store(store)
                .name(menuDto.getName())
                .price(menuDto.getPrice())
                .description(menuDto.getDescription())
                .isSoldOut(menuDto.isSoldOut())
                .build();


        when(storeRepository.findByIdAndIsDeletedFalseOrElseThrow(storeId)).thenReturn(store);
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        // when
        MenuResponseDto responseDto = menuService.createMenu(menuDto);

        // then
        assertThat(responseDto.getStoreId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("Test Menu");
        assertThat(responseDto.getPrice()).isEqualTo(12000);
        assertThat(responseDto.getDescription()).isEqualTo("Delicious test menu");
        assertThat(responseDto.isSoldOut()).isFalse();
    }

    @Test
    void updateMenuTest() {
        // given
        MenuDto menuDto = new MenuDto(1L, "Updated Menu", 15000, "Updated Description", true);
        Menu existingMenu = Menu.builder()
                .store(store)
                .name("Old Menu")
                .price(10000)
                .description("Old Description")
                .isSoldOut(false)
                .build();

        when(menuRepository.findByIdOrElseThrow(1L)).thenReturn(existingMenu);
        when(storeRepository.findByIdAndIsDeletedFalseOrElseThrow(1L)).thenReturn(store);

        // when
        MenuResponseDto responseDto = menuService.updateMenu(1L, menuDto);

        // then
        assertThat(responseDto.getStoreId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("Updated Menu");
        assertThat(responseDto.getPrice()).isEqualTo(15000);
        assertThat(responseDto.getDescription()).isEqualTo("Updated Description");
        assertThat(responseDto.isSoldOut()).isTrue();
    }

    @Test
    void deleteMenuTest() {
        // given
        Menu menu = Menu.builder()
                .store(store)
                .name("Test Menu")
                .price(12000)
                .description("Delicious test menu")
                .isSoldOut(false)
                .build();

        when(menuRepository.findByIdOrElseThrow(1L)).thenReturn(menu);

        // when
        menuService.deleteMenu(1L);

        // then
        verify(menuRepository, times(1)).delete(menu);
    }
}
