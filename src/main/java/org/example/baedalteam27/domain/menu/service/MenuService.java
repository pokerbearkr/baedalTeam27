package org.example.baedalteam27.domain.menu.service;

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
}
