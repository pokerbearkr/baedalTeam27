package org.example.baedalteam27.domain.order.repository;

import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId AND order_status = :status ORDER BY ordered_time DESC LIMIT 1", nativeQuery = true)
    Optional<Order> findLatestOrderByUserIdAndStatus(@Param("userId") Long userId, @Param("status")OrderStatus status);
    default Order getFindLatestOrderByUserIdAndStatus(Long userId, OrderStatus status){
        return findLatestOrderByUserIdAndStatus(userId, status)
                .orElseThrow(()-> new RuntimeException("주문이 없습니다."));
    }

    @Query("SELECT o FROM Order o WHERE o.store = :storeId AND o.orderStatus = :status ORDER BY o.orderedTime DESC")
    List<Order> findOrdersByStoreIdAndOrderStatus(@Param("storeId") Long storeId, @Param("status")OrderStatus orderStatus);


    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.orderStatus = :status")
    Optional<Order> findByIdAndOrderStatus(@Param("orderId")Long orderId, @Param("status")OrderStatus orderStatus);
    default Order getFindByIdAndOrderStatus(Long orderId, OrderStatus orderStatus){
        return findByIdAndOrderStatus(orderId, orderStatus)
                .orElseThrow(() -> new RuntimeException("주문이 없습니다."));
    }

}
