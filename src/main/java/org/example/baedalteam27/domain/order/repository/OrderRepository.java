package org.example.baedalteam27.domain.order.repository;

import org.example.baedalteam27.domain.order.entity.Order;
import org.example.baedalteam27.domain.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT * FROM orders WHERE user_id = :userId AND order_status = :status ORDER BY ordered_time DESC LIMIT 1", nativeQuery = true)
    Optional<Order> findLatestOrderByUserIdAndStatus(@Param("userId") Long userId, @Param("status")OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.store = :storeId AND o.orderStatus = :status ORDER BY o.orderedTime DESC")
    List<Order> findOrdersByStoreIdAndOrderStatus(@Param("storeId") Long storeId, @Param("status")OrderStatus orderStatus);
}
