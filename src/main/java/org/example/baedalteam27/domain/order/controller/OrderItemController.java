package org.example.baedalteam27.domain.order.controller;

import org.example.baedalteam27.global.jwt.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class OrderItemController {
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> getShoppingCart(@LoginUser Long userId){
        OrderItemService.getShoppingCart(userId);
    }
}
