package org.example.baedalteam27.domain.order.repository;

import org.example.baedalteam27.domain.order.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderItems,Long> {
}
