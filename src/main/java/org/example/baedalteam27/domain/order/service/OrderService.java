package org.example.baedalteam27.domain.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.order.repository.OrderDetailsRepository;
import org.example.baedalteam27.domain.shoppingCart.dto.response.ShoppingCartResponse;
import org.example.baedalteam27.domain.shoppingCart.repository.ShoppingCartRepository;
import org.example.baedalteam27.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;

    private final ShoppingCartRepository shoppingCartRepository;

    /*
    유저를 매개변수로 받아, 유저의 유일한 장바구니를 호출 -> Order 및 details 테이블 저장 -> 주문 완료 리턴
     */
    @Transactional
    public void order(Long userId){
        //TODO: 장바구니 호출시키기~

        OrderRepository.save();
        List<ShoppingCartResponse> shoppingCart

    }
}
