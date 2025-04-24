package org.example.baedalteam27.domain.shoppingCart.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.shoppingCart.dto.request.AddShoppingCartRequest;
import org.example.baedalteam27.domain.shoppingCart.dto.response.ShoppingCartResponse;
import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.example.baedalteam27.domain.shoppingCart.repository.ShoppingCartRepository;
import org.example.baedalteam27.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Transactional
    public void add(Long userId, AddShoppingCartRequest dto){
        Store store =storeRepository.findById(dto.getStore());

        //TODO: getMenu 로직 pull받고 수정
        ShoppingCart cart = new ShoppingCart(
                userRepository.findById(userId),
                store.getId(),
                store.getMenu(),
                dto.getQuantity()
                );
        ShoppingCartRepository.save(cart);
    }

    public List<ShoppingCartResponse> getShoppingCart(Long userid){
        List<ShoppingCart> shoppingCartEntity = shoppingCartRepository.findByUserIdWithStoreAndMenu(userid);

        //DTO 변환
        List<ShoppingCartResponse> shoppingCartResponses = shoppingCartEntity.stream()
                .map(cart -> new ShoppingCartResponse(cart.getMenuItem().getName(),cart.getMenuItem().getPrice(),cart.getQuantity()))
                .collect(Collectors.toList());

        return shoppingCartResponses;
    }

    @Transactional
    public void deleteOneMenu(Long userId, Long menuId) {
        shoppingCartRepository.deleteByUserIdAndMenuId(userId, menuId);
    }
    @Transactional
    public void deleteShoppingCart(Long userId){
        shoppingCartRepository.deleteAll();
    }
}
