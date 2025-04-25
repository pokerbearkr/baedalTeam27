package org.example.baedalteam27.domain.order.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.baedalteam27.domain.order.dto.response.OrderResponse;
import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.order.entity.OrderDetails;
import org.example.baedalteam27.domain.order.entity.OrderStatus;
import org.example.baedalteam27.domain.store.entity.Store;
import org.example.baedalteam27.domain.order.repository.OrderDetailsRepository;
import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.example.baedalteam27.domain.shoppingCart.repository.ShoppingCartRepository;
import org.example.baedalteam27.domain.order.repository.OrderRepository;
import org.springframework.stereotype.Service;


import org.example.baedalteam27.domain.store.repository.StoreRepository;




import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        // ShoppingCart 는 User 와 Store 이 중복 저장돼있음 -> get(아무인덱스)
        Order order = new Order(cart.get(0).getUser(),
                cart.get(0).getStore(),
                deliveryLocation,
                LocalDateTime.now(),
                OrderStatus.PENDING);

        orderRepository.save(order);

        // 상세 주문 저장
        // 방금 주문한 유일한 칼럼을 userId와 OrderStatus, 그리고 Limit 1로 찾음
        Order theOrder = orderRepository
                .getFindLatestOrderByUserIdAndStatus(userId, OrderStatus.PENDING);

        ArrayList<OrderDetails> orderDetails = cart.stream()
                .map(c -> new OrderDetails(theOrder, c.getMenu(), c.getQuantity()))
                .collect(Collectors.toCollection(ArrayList::new));

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
        Order willBeCanceledOrder = orderRepository.getFindLatestOrderByUserIdAndStatus(userId, OrderStatus.PENDING);


        willBeCanceledOrder.setOrderStatus(orderStatus);
        willBeCanceledOrder.setOrderedTime(LocalDateTime.now());
        return orderStatus;
    }

    public ArrayList<OrderResponse> getAllOrdersForStore(Long userId){
        Store store = storeRepository.findByUserIdAndIsDeletedFalseOrElseThrow(userId);

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

    public OrderResponse getOneOrder(Long userId, Long orderId){
        Order oneOrder = orderRepository.getFindByIdAndOrderStatus(orderId, OrderStatus.PENDING);

        return new OrderResponse(oneOrder.getId(),
                oneOrder.getLocation(),
                oneOrder.getOrderedTime(),
                oneOrder.getOrderStatus());
    }
}
