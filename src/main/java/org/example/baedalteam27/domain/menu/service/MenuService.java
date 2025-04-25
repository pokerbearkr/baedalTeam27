package org.example.baedalteam27.domain.menu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.menu.dto.MenuDto;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.menu.repository.MenuRepository;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public Menu createMenu(MenuDto menuDto) {
        Store store = storeRepository.findById(menuDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다"));

        Menu menu = Menu.builder()
                .store(store)
                .name(menuDto.getName())
                .price(menuDto.getPrice())
                .description(menuDto.getDescription())
                .isSoldOut(menuDto.isSoldOut())
                .build();

        return menuRepository.save(menu);
    }

    @Transactional
    public Menu updateMenu(Long menuId, MenuDto menuDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다"));

        Store store = storeRepository.findById(menuDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다"));

        menu.setStore(store);
        menu.setName(menuDto.getName());
        menu.setPrice(menuDto.getPrice());
        menu.setDescription(menuDto.getDescription());
        menu.setSoldout(menuDto.isSoldOut());

        return menu;
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴를 찾을 수 없습니다"));
        menuRepository.delete(menu);
    }
}
