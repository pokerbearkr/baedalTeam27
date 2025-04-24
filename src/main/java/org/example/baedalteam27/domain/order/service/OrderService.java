package org.example.baedalteam27.domain.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.order.dto.response.OrderResponse;
import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.order.entity.OrderDetails;
import org.example.baedalteam27.domain.order.entity.OrderStatus;
import org.example.baedalteam27.domain.order.repository.OrderDetailsRepository;
import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.example.baedalteam27.domain.shoppingCart.repository.ShoppingCartRepository;
import org.example.baedalteam27.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final StoreRepository storeRepository;


    /*
    유저를 매개변수로 받아, 유저의 유일한 장바구니를 호출 -> Order 및 details 테이블 저장 -> 주문 완료 리턴
     */
    @Transactional
    public void doOrder(Long userId, String deliveryLocation){
        List<ShoppingCart> cart = shoppingCartRepository.findByUserIdWithStoreAndMenu(userId);

        // 주문 정보 저장
        Order order = new Order(cart.get(0).getUser(),
                cart.get(0).getStore(),
                deliveryLocation,
                LocalDateTime.now(),
                OrderStatus.PENDING);

        orderRepository.save(order);

        // 상세 주문 저장
        // 방금 저장한 주문의 칼럼 하나를 userId와 OrderStatus, 그리고 Limit 1로 찾음
        Order theOrder = orderRepository
                .findLatestOrderByUserIdAndStatus(userId, OrderStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("주문이 없습니다."));

        ArrayList<OrderDetails> orderDetails = cart.stream()
                .map(c -> new OrderDetails(theOrder, c.getMenu(), c.getQuantity())
                        .collect(Collectors.toCollection(ArrayList::new)));

        //TODO: 시간나면 BATCH
        orderDetailsRepository.saveAll(orderDetails);

        // 주문 했으니까 장바구니 비우기
        shoppingCartRepository.deleteAll();
    }
    /*
    주문 상태를 변경하여 주문 취소, 거절, 승인.
     */
    @Transactional
    public OrderStatus orderStatusChange(Long userId, OrderStatus orderStatus){
        Order willBeCanceledOrder = orderRepository.findLatestOrderByUserIdAndStatus(userId, OrderStatus.PENDING)
                .orElseThrow(()-> new RuntimeException("주문이 없습니다"));

        willBeCanceledOrder.setOrderStatus(orderStatus);
        willBeCanceledOrder.setOrderedTime(LocalDateTime.now());
        return orderStatus;
    }

    public ArrayList<OrderResponse> getAllOrdersForStore(Long userId){
        Optional<Store> store = storeRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("가게가 없습니다."));

        List<Order> orders = orderRepository.findOrdersByStoreIdAndOrderStatus(store.getId(), OrderStatus.PENDING);

        ArrayList<OrderResponse> ordersResponses = orders.stream()
                .map(o -> new OrderResponse(
                        o.getId(),
                        o.getLocation(),
                        o.getOrderedTime(),
                        o.getOrderStatus()
                ))
                .collect(Collectors.toCollection(ArrayList::new));

        return ordersResponses;
    }

    public OrderResponse getOneOrder(Long userId){
        Order oneOrder = orderRepository.findLatestOrderByUserIdAndStatus(userId, OrderStatus.PENDING)
                .orElseThrow(() -> new RuntimeException("주문이 없습니다."));

        return new OrderResponse(oneOrder.getId(),
                oneOrder.getLocation(),
                oneOrder.getOrderedTime(),
                oneOrder.getOrderStatus());
    }
}
