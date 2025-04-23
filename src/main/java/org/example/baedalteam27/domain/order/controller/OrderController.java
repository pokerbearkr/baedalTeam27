package org.example.baedalteam27.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.example.baedalteam27.domain.order.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final shoppingCartService shoppingCartService;

    @PostMapping
    public ResponseEntity<Void> order(@LoginUser Long userId){
        orderService.order(userId);


    return
    }

}
