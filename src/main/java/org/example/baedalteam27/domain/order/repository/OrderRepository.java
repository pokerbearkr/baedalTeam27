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
    @Query(value = "SELECT * FROM Orders  WHERE user_id = :userId AND order_status = :status ORDER BY ordered_time DESC LIMIT 1", nativeQuery = true)
    Optional<Order> findLatestOrderByUserIdAndStatus(@Param("userId") Long userId, @Param("status") String status);
    default Order getFindLatestOrderByUserIdAndStatus(Long userId, String status){
        return findLatestOrderByUserIdAndStatus(userId, status)
                .orElseThrow(()-> new RuntimeException("주문이 없습니다."));
    }

    @Query("SELECT o FROM Order o WHERE o.store.id = :storeId AND o.orderStatus = :status ORDER BY o.orderedTime DESC")
    List<Order> findOrdersByStoreIdAndOrderStatus(@Param("storeId") Long storeId, @Param("status")OrderStatus orderStatus);


    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.orderStatus = :status")
    Optional<Order> findByIdAndOrderStatus(@Param("orderId")Long orderId, @Param("status")String orderStatus);
    default Order getFindByIdAndOrderStatus(Long orderId, String orderStatus){
        return findByIdAndOrderStatus(orderId, orderStatus)
                .orElseThrow(() -> new RuntimeException("주문이 없습니다."));
    }

    @Query("SELECT o FROM Order o JOIN FETCH o.store " +
            "JOIN FETCH o.user "+
            "WHERE o.id =:orderId")
    default Order findByIdOrElseThrow(@Param("orderId")Long id){
        return findById(id)
                .orElseThrow(()-> new RuntimeException("주문이 없습니다"));
    }
}
