package org.example.baedalteam27.domain.order.repository;

import org.example.baedalteam27.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Long, Order> {
}
