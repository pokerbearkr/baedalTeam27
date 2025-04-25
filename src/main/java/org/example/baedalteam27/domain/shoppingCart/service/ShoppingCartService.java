package org.example.baedalteam27.domain.shoppingCart.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.menu.repository.MenuRepository;
import org.example.baedalteam27.domain.shoppingCart.dto.request.AddShoppingCartRequest;
import org.example.baedalteam27.domain.shoppingCart.dto.response.ShoppingCartResponse;
import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.example.baedalteam27.domain.shoppingCart.repository.ShoppingCartRepository;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.menu.entity.Menu;
import org.example.baedalteam27.domain.store.repository.StoreRepository;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public void add(Long userId, AddShoppingCartRequest dto){
        Store store = storeRepository.findByIdOrElseThrow(userId);

        List<Menu> menuList = store.getMenus();

        Menu pickedMenu = menuList.stream()
                .filter(m -> m.getId().equals(dto.getMenu()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("메뉴가 없습니다."));

        ShoppingCart cart = new ShoppingCart(
                userRepository.getUserByUserId(userId),
                store,
                pickedMenu,
                dto.getQuantity()
                );
        shoppingCartRepository.save(cart);
    }

    public List<ShoppingCartResponse> getShoppingCart(Long userid){
        List<ShoppingCart> shoppingCartEntity = shoppingCartRepository.findByUserIdWithStoreAndMenu(userid);

        //DTO 변환
        List<ShoppingCartResponse> shoppingCartResponse = shoppingCartEntity.stream()
                .map(cart -> new ShoppingCartResponse(cart.getStore().getStoreName(),cart.getMenu().getName(), cart.getMenu().getPrice(),cart.getQuantity()))
                .toList();

        return shoppingCartResponse;
    }

    @Transactional
    public void deleteOneMenu(Long userId, Long menuId) {
        shoppingCartRepository.deleteByUserIdAndMenuId(userId, menuId);
    }
    @Transactional
    public void deleteShoppingCart(Long userId){
        shoppingCartRepository.deleteAllByUserId(userId);
    }
}
