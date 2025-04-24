package org.example.baedalteam27.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.order.dto.response.OrderResponse;
import org.example.baedalteam27.domain.order.entity.OrderStatus;
import org.example.baedalteam27.global.jwt.LoginUser;
import org.example.baedalteam27.domain.order.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Void> order(@LoginUser Long userId, @RequestBody String deliveryLocation){
        orderService.doOrder(userId, deliveryLocation);
        return ResponseEntity.ok().build();
    }

    /*
      주문 취소, 승인 등. 성공시 200 코드와 함꼐 변경한 상태(Enum) 반환
     */
    @PostMapping
    public ResponseEntity<OrderStatus> orderStatusChange(@LoginUser Long userId, OrderStatus orderStatus){
        return ResponseEntity.ok(orderService.orderStatusChange(userId, orderStatus));
    }

    @GetMapping
    public ResponseEntity<ArrayList<OrderResponse>> getAllOrdersForStore(@LoginUser Long userId){
        return ResponseEntity.ok(orderService.getAllOrdersForStore(userId));
    }
    @GetMapping
    public ResponseEntity<OrderResponse> getOneOrder(@LoginUser Long userId){
        return ResponseEntity.ok(orderService.getOneOrder(userId));
    }
}
