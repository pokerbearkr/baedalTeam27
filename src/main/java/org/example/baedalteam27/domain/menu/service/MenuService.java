package org.example.baedalteam27.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.menu.dto.MenuDto;
import org.example.baedalteam27.domain.menu.dto.MenuResponseDto;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.menu.repository.MenuRepository;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    public MenuResponseDto createMenu(MenuDto menuDto) {
        Store store = storeRepository.findByIdAndIsDeletedFalseOrElseThrow(menuDto.getStoreId());

        Menu menu = Menu.builder()
                .store(store)
                .name(menuDto.getName())
                .price(menuDto.getPrice())
                .description(menuDto.getDescription())
                .isSoldOut(menuDto.isSoldOut())
                .build();
        Menu save = menuRepository.save(menu);
        return new MenuResponseDto(save.getId(), save.getStore().getId(), save.getName(), save.getPrice(), save.getDescription(), save.isSoldOut());
    }

    @Transactional
    public MenuResponseDto updateMenu(Long menuId, MenuDto menuDto) {
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        Store store = storeRepository.findByIdAndIsDeletedFalseOrElseThrow(menuDto.getStoreId());

        menu.setStore(store);
        menu.setName(menuDto.getName());
        menu.setPrice(menuDto.getPrice());
        menu.setDescription(menuDto.getDescription());
        menu.setSoldout(menuDto.isSoldOut());


        return new MenuResponseDto(menuId, menuDto.getStoreId(), menuDto.getName(), menuDto.getPrice(), menuDto.getDescription(), menuDto.isSoldOut());
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findByIdOrElseThrow(menuId);
        menuRepository.delete(menu);
    }
}
