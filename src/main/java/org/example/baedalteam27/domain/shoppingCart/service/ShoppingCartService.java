package org.example.baedalteam27.domain.shoppingCart.service;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.shoppingCart.dto.response.ShoppingCartResponse;
import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.example.baedalteam27.domain.shoppingCart.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;

    public List<ShoppingCartResponse> getShoppingCart(Long userid){
        List<ShoppingCart> shoppingCartEntity = shoppingCartRepository.findByUserId(userid)
                    .orElseThrow(() -> new RuntimeException("장바구니가 비어있습니다."));

        //DTO 변환
        List<ShoppingCartResponse> shoppingCartResponses = shoppingCartEntity.stream()
                .map(cart -> new ShoppingCartResponse(cart.getMenuItem(),cart.getQuantity()))
                .collect(Collectors.toList());

        return shoppingCartResponses;
    }
}
