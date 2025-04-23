package org.example.baedalteam27.domain.shoppingCart.repository;

import org.example.baedalteam27.domain.shoppingCart.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<List<ShoppingCart>> findByUserId(Long userId);
}
