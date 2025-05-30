package org.example.baedalteam27.domain.order.repository;

import org.example.baedalteam27.domain.order.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {
    List<OrderDetails> findByOrderId(Long orderId);
}
