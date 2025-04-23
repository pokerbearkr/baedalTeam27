package org.example.baedalteam27.domain.shoppingCart.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.shoppingCart.dto.response.ShoppingCartResponse;
import org.example.baedalteam27.domain.shoppingCart.service.ShoppingCartService;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<List<ShoppingCartResponse>> getShoppingCart(@LoginUser Long userId){
        return ResponseEntity.ok(shoppingCartService.getShoppingCart(userId));
    }
}
