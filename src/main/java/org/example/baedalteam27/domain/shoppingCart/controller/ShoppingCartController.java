package org.example.baedalteam27.domain.shoppingCart.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.shoppingCart.dto.request.AddShoppingCartRequest;
import org.example.baedalteam27.domain.shoppingCart.dto.response.ShoppingCartResponse;
import org.example.baedalteam27.domain.shoppingCart.service.ShoppingCartService;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<Void> add(@LoginUser Long userId, @RequestBody AddShoppingCartRequest dto) {
        shoppingCartService.add(userId, dto);
        return ResponseEntity.ok().build();
    }
    // 장바구니 불러오기
    @GetMapping
    public ResponseEntity<List<ShoppingCartResponse>> getShoppingCart(@LoginUser Long userId){
        return ResponseEntity.ok(shoppingCartService.getShoppingCart(userId));
    }
    // 장바구니에서 메뉴 하나 없애기
    @DeleteMapping("{userId}/items/{menuId}")
    public ResponseEntity<Void> deleteOneMenu(@LoginUser Long userId, @RequestParam Long menuId){
        shoppingCartService.deleteOneMenu(userId, menuId);
        return ResponseEntity.ok().build();
    }
    // 장바구니 비우기
    @DeleteMapping
    public ResponseEntity<Void> deleteShoppingCart(@LoginUser Long userId){
        shoppingCartService.deleteShoppingCart(userId);
        return ResponseEntity.ok().build();
    }
}
